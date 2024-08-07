package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.dto.SeatBookingDto;
import com.example.CatchStudy.domain.dto.response.BookingResponseDto;
import com.example.CatchStudy.domain.dto.response.KakaoApproveResponseDto;
import com.example.CatchStudy.domain.dto.response.KakaoCancelResponseDto;
import com.example.CatchStudy.domain.dto.response.KakaoReadyResponseDto;
import com.example.CatchStudy.domain.entity.*;
import com.example.CatchStudy.global.enums.BookingStatus;
import com.example.CatchStudy.global.enums.PaymentStatus;
import com.example.CatchStudy.global.enums.SeatType;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.global.kakaopay.KakaoPayProperties;
import com.example.CatchStudy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final UsersRepository usersRepository;
    private final StudyCafeRepository studyCafeRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final BookingService bookingService;
    private final BookedRoomInfoRepository bookedRoomInfoRepository;
    private final QuartzSchedulerService quartzSchedulerService;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BookingResponseDto kakaoPayReady(SeatBookingDto dto, Long userId) { //카카오페이에 결제 요청
        Users user = usersRepository.findByUserId(userId).orElseThrow(() -> new CatchStudyException(ErrorCode.USER_NOT_FOUND));
        StudyCafe studyCafe = studyCafeRepository.findByCafeId(dto.getCafeId()).orElseThrow(() -> new CatchStudyException(ErrorCode.STUDYCAFE_NOT_FOUND));
        Long paymentId = null;

        if (dto.getType() == SeatType.seat) {
            paymentId = bookingService.saveSeatBooking(dto,user,studyCafe);
        }else if(dto.getType() == SeatType.room){
            paymentId = bookingService.saveRoomBooking(dto,user);
        }

        Payment payment = paymentRepository.findByPaymentId(paymentId).orElseThrow(() -> new CatchStudyException(ErrorCode.PAYMENT_NOT_FOUND));

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", KakaoPayProperties.cid);                                    // 가맹점 코드(테스트용)
        parameters.put("partner_order_id", String.valueOf(paymentId));                    // 주문번호(paymentId)
        parameters.put("partner_user_id", String.valueOf(userId));                        // 회원 아이디
        parameters.put("item_name", studyCafe.getCafeName());                             // 스터디카페 이름
        parameters.put("quantity", "1");                                                  // 상품 수량
        parameters.put("total_amount", String.valueOf(dto.getAmount()));                  // 상품 총액
        parameters.put("tax_free_amount", "0");
        parameters.put("approval_url", "http://3.39.182.9:8080/api/payment/success" + "/" + user.getUserId() + "/" + paymentId); // 성공 시 redirect url+{userId}+{seatId}
        parameters.put("cancel_url", "http://3.39.182.9:8080/api/payment/cancel/" + paymentId); // 취소 시 redirect url
        parameters.put("fail_url", "http://3.39.182.9:8080/api/payment/fail/" + paymentId); // 실패 시 redirect url

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        RestTemplate restTemplate = new RestTemplate();

        KakaoReadyResponseDto kakaoReady = restTemplate.postForObject(
                KakaoPayProperties.readyUrl,
                requestEntity,
                KakaoReadyResponseDto.class
        );
        payment.updateTid(kakaoReady.getTid()); //tid payment 테이블에 저장

        return kakaoReady.toDto();
    }

    @Transactional
    public KakaoApproveResponseDto kakaoPayApprove(String pgToken, Long userId, Long paymentId) { //카카오 페이에 승인 요청
        Payment payment = paymentRepository.findByPaymentId(paymentId).orElseThrow(() -> new CatchStudyException(ErrorCode.PAYMENT_NOT_FOUND));
        Booking booking = payment.getBooking();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", KakaoPayProperties.cid);
        parameters.put("tid", payment.getTid());
        parameters.put("partner_order_id", String.valueOf(paymentId));
        parameters.put("partner_user_id", String.valueOf(userId));
        parameters.put("pg_token", pgToken);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        RestTemplate restTemplate = new RestTemplate();

        KakaoApproveResponseDto kakaoApprove = restTemplate.postForObject(
                KakaoPayProperties.approveUrl,
                requestEntity,
                KakaoApproveResponseDto.class
        );

        //좌석,room 사용중으로 변경,  booking 테이블 변경 (상태,code), payment 테이블 변경 (amount,승인시간,status)
        String code = makeCode();
        booking.completeBooking(BookingStatus.beforeEnteringRoom, makeCode());
        payment.approvePayment(kakaoApprove.getApproved_at(), PaymentStatus.approve, kakaoApprove.getAmount().getTotal());

        if (booking.getBookedRoomInfo() == null) { //좌석 일 때

            try {
                quartzSchedulerService.scheduleBookingSeatStatusCheck(booking.getBookingId(), payment); //30 분 후 좌석 상태 확인 작업 스케줄;
            } catch (SchedulerException e) {
                log.error(e.getMessage());
                throw new CatchStudyException(ErrorCode.QUARTZ_SCHEDULER_ERROR);
            }

        } else {

            try {
                quartzSchedulerService.scheduleRoomEnterUpdate(booking.getBookingId(), booking.getStartTime()); //스터디룸 입실 시간이 되었을 때 작업 스케줄

            } catch (SchedulerException e) {
                throw new CatchStudyException(ErrorCode.QUARTZ_SCHEDULER_ERROR);
            }

            try {
                quartzSchedulerService.scheduleCheckOutRoomBooking(booking.getBookingId(), booking.getEndTime()); //스터디룸 퇴실 시간이 되었을 때 작업 스케줄

            } catch (SchedulerException e) {
                throw new CatchStudyException(ErrorCode.QUARTZ_SCHEDULER_ERROR);
            }
        }

        return kakaoApprove;
    }

    @Transactional
    public void kakaoCancel(Long bookingId) {
        Payment payment = paymentRepository.findByBookingBookingId(bookingId).orElseThrow(() -> new CatchStudyException(ErrorCode.PAYMENT_NOT_FOUND));
        BookedRoomInfo bookedRoomInfo = payment.getBooking().getBookedRoomInfo();
        Room room = bookedRoomInfo.getRoom();
        LocalDateTime maxPossibleTime = null;
        if (room.getCancelAvailableTime() == null) {
            maxPossibleTime = bookedRoomInfo.getBookingDateStartTime();
        } else {
            maxPossibleTime = bookedRoomInfo.getBookingDateStartTime().minusMinutes(room.getCancelAvailableTime());
        }
        if (LocalDateTime.now().isAfter(maxPossibleTime)) { // 취소 가능한 시간을 넘었을 경우
            throw new CatchStudyException(ErrorCode.CANCEL_POSSIBLE_TIME_PASSED);
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", KakaoPayProperties.cid);
        parameters.put("tid", payment.getTid());
        parameters.put("cancel_amount", String.valueOf(payment.getAmount()));
        parameters.put("cancel_tax_free_amount", "0");

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        RestTemplate restTemplate = new RestTemplate();

        KakaoCancelResponseDto kakaoCancel = restTemplate.postForObject(
                KakaoPayProperties.cancelUrl,
                requestEntity,
                KakaoCancelResponseDto.class
        );

        // 결제 환불 후 예약 스터디룸 정보 삭제
        payment.getBooking().cancelBooking(BookingStatus.canceled);
        payment.cancelPayment(PaymentStatus.cancel, kakaoCancel.getCanceled_at());
        payment.getBooking().deleteRoomInfo();
        bookedRoomInfoRepository.delete(bookedRoomInfo);
    }

    private void printInfo() {
        boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
        log.info("tx active={}", txActive);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        String auth = "SECRET_KEY " + KakaoPayProperties.secretKey;
        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/json");
        return httpHeaders;
    }

    private String makeCode() { //4자리 코드 생성
        Random random = new Random();
        int createNum = 0;
        String ranNum = "";
        int letter = 4;
        String resultNum = "";

        for (int i = 0; i < letter; i++) {
            createNum = random.nextInt(9);
            ranNum = Integer.toString(createNum);
            resultNum += ranNum;
        }
        return resultNum;
    }
}
