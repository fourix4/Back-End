package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.dto.*;
import com.example.CatchStudy.domain.dto.response.*;
import com.example.CatchStudy.domain.entity.*;
import com.example.CatchStudy.global.enums.BookingStatus;
import com.example.CatchStudy.global.enums.ImageType;
import com.example.CatchStudy.global.enums.PaymentStatus;
import com.example.CatchStudy.global.enums.SeatType;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UsersRepository usersRepository;
    private final SeatRepository seatRepository;
    private final PaymentRepository paymentRepository;
    private final RoomRepository roomRepository;
    private final BookedRoomInfoRepository bookedRoomInfoRepository;

    private final StudyCafeRepository studyCafeRepository;
    private final CafeImageRepository cafeImageRepository;
    private final UsageFeeRepository usageFeeRepository;

    private final QuartzSchedulerService quartzSchedulerService;

    private final RedissonClient redissonClient;

    private final RedissonService redissonService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long saveSeatBooking(SeatBookingDto dto,Users user,StudyCafe studyCafe){
        Payment payment = null;

        checkAvailableSeatsTime(studyCafe, dto.getTime());

        try {
            Seat seat = seatRepository.findBySeatIdOptimisticLock(dto.getSeatId()).orElseThrow(() -> new CatchStudyException(ErrorCode.SEAT_NOT_FOUND));
            if(!seat.getIsAvailable()){
                throw new CatchStudyException(ErrorCode.BOOKING_NOT_AVAILABLE);
            }
            seat.updateSeatStatus(false);
            Booking booking = bookingRepository.save(Booking.of(dto.getTime(), user, studyCafe, seat));
            payment =paymentRepository.save(Payment.of(dto.getPaymentType(), booking, PaymentStatus.ready));

        }catch (ObjectOptimisticLockingFailureException e){
            throw new CatchStudyException(ErrorCode.BOOKING_NOT_AVAILABLE);
        }
        return payment.getPaymentId();
    }

    @Transactional
    public Long saveRoomBooking(SeatBookingDto dto,Users user){
        Payment payment = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(dto.getStartTime(), formatter);
        if(localDateTime.isBefore(LocalDateTime.now())){ // 현재 날짜 이전을 선택하면
            throw new CatchStudyException(ErrorCode.BOOKING_NOT_AVAILABLE);
        }
        payment = redissonService.saveRoomBooking(dto,dto.getRoomId(),user);

        return payment.getPaymentId();
    }

    @Transactional(readOnly = true)
    public SeatingChartResponseDto showSeatingChart(Long cafeId) {
        StudyCafe studyCafe = studyCafeRepository.findByCafeId(cafeId).orElseThrow(() -> new CatchStudyException(ErrorCode.STUDYCAFE_NOT_FOUND));
        String seatingChart = cafeImageRepository.findByStudyCafeCafeIdAndImageType(cafeId, ImageType.seatingChart).orElseThrow(
                () -> new CatchStudyException(ErrorCode.STUDYCAFE_IMAGE_NOT_FOUND)
        ).getCafeImage(); // 좌석 배치도 url
        List<SeatDto> seats = seatRepository.findAllByStudyCafeCafeId(cafeId) //좌석 정보
                .stream()
                .sorted(Comparator.comparing(Seat::getSeatId))
                .map(SeatDto::from)
                .collect(Collectors.toList());

        List<RoomDto> rooms = roomRepository.findAllByStudyCafeCafeId(cafeId) //스터디룸 정보
                .stream().sorted(Comparator.comparing(Room::getRoomId))
                .map(RoomDto::from)
                .collect(Collectors.toList());
        List<UsageFeeDto> usageFee = usageFeeRepository.findAllByStudyCafe_CafeId(cafeId)
                .stream().sorted(Comparator.comparing(UsageFee::getHours))
                .map(UsageFeeDto::from)
                .collect(Collectors.toList());
        return SeatingChartResponseDto.toResponseDto(
                seatingChart,
                seats,
                rooms,
                usageFee
        );
    }

    @Transactional(readOnly = true)
    public BookingHistoryResponseDto getBookingHistory(Long userId) { // 최근 30개 예약 내역 조회
        List<Payment> payments = paymentRepository.find30PaymentFetchJoin(userId);
        List<BookingHistoryDto> bookingHistoryDtos = payments.stream().map(payment -> {
            Booking booking = payment.getBooking();
            StudyCafe studyCafe = booking.getStudyCafe();
            String cafeImage =  cafeImageRepository.findByStudyCafeCafeIdAndImageType(studyCafe.getCafeId(),ImageType.thumbnail)
                    .map(CafeImage::getCafeImage)
                    .orElse("");

            return BookingHistoryDto.toDto(
                    booking.getBookingId(),
                    studyCafe.getCafeId(),
                    studyCafe.getCafeName(),
                    cafeImage,
                    booking.getSeatType(),
                    studyCafe.getAddress(),
                    payment.getPaymentTime(),
                    booking.getStartTime(),
                    booking.getEndTime(),
                    payment.getAmount(),
                    booking.getStatus().getMessage()
            );
        }).collect(Collectors.toList());

        return BookingHistoryResponseDto.toResponseDto(bookingHistoryDtos);
    }

    @Transactional(readOnly = true)
    public BookingHistoryByDateResponseDto getBookingHistoryByDate(Long userId, LocalDate startDate, LocalDate endDate, int page) { // 일정 기간 예약 내역 조회
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Order.asc("paymentTime")));

        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.atTime(LocalTime.MAX);

        Page<Payment> paymentsPage = paymentRepository.findBookingPaymentTimeBetweenFetchJoin(userId,startTime,endTime,pageable);
        List<BookingHistoryDto> bookingDtos = paymentsPage.getContent().stream().map(payment -> {
            Booking booking = payment.getBooking();
            StudyCafe studyCafe = booking.getStudyCafe();

            String cafeImage = cafeImageRepository.findByStudyCafeCafeIdAndImageType(studyCafe.getCafeId(),ImageType.thumbnail)
                    .map(CafeImage::getCafeImage)
                    .orElse("");

            return BookingHistoryDto.toDto(
                    booking.getBookingId(),
                    studyCafe.getCafeId(),
                    studyCafe.getCafeName(),
                    cafeImage,
                    booking.getSeatType(),
                    studyCafe.getAddress(),
                    payment.getPaymentTime(),
                    booking.getStartTime(),
                    booking.getEndTime(),
                    payment.getAmount(),
                    booking.getStatus().getMessage()
            );
        }).collect(Collectors.toList());

        return BookingHistoryByDateResponseDto.toResponseDto(bookingDtos);
    }

    @Transactional(readOnly = true)
    public AvailableBookingResponseDto getAvailableBooking(Long userId) { //현재 예약 내용 조회
        List<AvailableBookingSeatDto> bookingSeatDtos = Optional.ofNullable(bookingRepository.getAvailableSeatsFetchJoin(userId))
                .orElseGet(Collections::emptyList)
                .stream()
                .map(booking -> {
                    Payment payment = paymentRepository.findByBookingBookingId(booking.getBookingId())
                            .orElseThrow(()-> new CatchStudyException(ErrorCode.PAYMENT_NOT_FOUND));
                    StudyCafe studyCafe = booking.getStudyCafe();

                    return AvailableBookingSeatDto.toDto(
                            booking.getBookingId(),
                            studyCafe.getCafeId(),
                            studyCafe.getCafeName(),
                            booking.getStatus().getMessage(),
                            payment.getAmount(),
                            studyCafe.getAddress(),
                            booking.getSeat().getSeatNumber(),
                            booking.getCode(),
                            payment.getPaymentTime(),
                            booking.getStartTime(),
                            booking.getEndTime(),
                            payment.getPaymentTime().plusMinutes(30)
                    );
                })
                .sorted(Comparator.comparing(AvailableBookingSeatDto::getPayment_time))
                .collect(Collectors.toList());

        List<AvailableBookingRoomDto> bookingRoomDtos = Optional.ofNullable(bookingRepository.getAvailableRoomsFetchJoin(userId))
                .orElseGet(Collections::emptyList)
                .stream()
                .map(booking -> {
                    Payment payment = paymentRepository.findByBookingBookingId(booking.getBookingId())
                            .orElseThrow(()-> new CatchStudyException(ErrorCode.PAYMENT_NOT_FOUND));
                    StudyCafe studyCafe = booking.getStudyCafe();

                    return AvailableBookingRoomDto.toDto(
                            booking.getBookingId(),
                            studyCafe.getCafeId(),
                            studyCafe.getCafeName(),
                            booking.getStatus().getMessage(),
                            payment.getAmount(),
                            studyCafe.getAddress(),
                            booking.getBookedRoomInfo().getRoom().getRoomName(),
                            booking.getCode(),
                            payment.getPaymentTime(),
                            booking.getStartTime(),
                            booking.getEndTime()
                    );
                })
                .sorted(Comparator.comparing(AvailableBookingRoomDto::getStart_time))
                .collect(Collectors.toList());

        return AvailableBookingResponseDto.toResponseDto(bookingSeatDtos, bookingRoomDtos);
    }

    @Transactional
    public void updateSeatStartTime(Long userId, String code) { // 입실 시간과 퇴실 시간 등록
        Booking booking = bookingRepository.findBookingBeforeEnteringSeat(userId, code).orElseThrow(
                () -> new CatchStudyException(ErrorCode.BOOKING_SEAT_NOT_FOUND)
        );

        LocalDateTime nowTime = LocalDateTime.now();
        booking.checkInSeatBooking(nowTime,booking.getTime());

        try {
            quartzSchedulerService.scheduleCheckOutSeatBooking(booking.getBookingId(),nowTime.plusMinutes(booking.getTime()));

        }catch (SchedulerException e){
            throw new CatchStudyException(ErrorCode.QUARTZ_SCHEDULER_ERROR);
        }
    }

    @Transactional
    public void checkOutSeat(Long bookingId) { // 퇴실 처리
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if (booking.getStatus() != BookingStatus.enteringRoom) {
            throw new CatchStudyException(ErrorCode.ENTERING_SEAT_NOT_FOUND);
        }
        booking.checkOutSeatBooking(LocalDateTime.now());
        booking.getSeat().checkOutSeat(true);
    }

    @Transactional
    public void deleteBooking(Long paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId).orElse(null);
        if(payment!=null){
            if(payment.getBooking().getSeat()==null){ //스터디룸
                bookedRoomInfoRepository.delete(payment.getBooking().getBookedRoomInfo());
            }else{
                payment.getBooking().getSeat().updateSeatStatus(true);
            }
            paymentRepository.deleteByPaymentId(paymentId);
        }
    }

    public void checkAvailableSeatsTime(StudyCafe studyCafe, Integer time){
        LocalTime now = LocalDateTime.now().toLocalTime();
        LocalTime openingTime = studyCafe.getOpeningHours();
        LocalTime closingTime = studyCafe.getClosedHours();

        // 카페 이용 시간 내에 있는지 확인
        boolean isOpen;

        if (closingTime.isAfter(openingTime)) {
            isOpen = now.isAfter(openingTime) && now.isBefore(closingTime);

        } else { //마감시간이 자정 이후 일 경우
            isOpen = now.isAfter(openingTime) || now.isBefore(closingTime);
        }
        if(!isOpen){
            throw new CatchStudyException(ErrorCode.BOOKING_NOT_AVAILABLE);
        }

        int requestedDurationMinutes = time + 35;
        LocalTime requestedEndTime = now.plusMinutes(requestedDurationMinutes);

        boolean canUse = true;

        if(closingTime.isAfter(openingTime)){//마감시간이 자정 이전 일 경우
            if(!requestedEndTime.isBefore(LocalTime.MIDNIGHT) && requestedEndTime.isBefore(openingTime)){ // 끝나는 시간이 자정 이후 영업 시작 시간 전이면(0시~9시)
                canUse = false;
            }
            else if(requestedEndTime.isBefore(LocalTime.of(23,59))){// 끝나는 시간이 자정 이전 일 때
                if(requestedEndTime.isAfter(closingTime)){
                    canUse = false;
                }
            }
        }else{
            if(!requestedEndTime.isBefore(LocalTime.MIDNIGHT) && requestedEndTime.isBefore(openingTime)){ // 끝나는 시간이 자정 이후 영업 시작 시간 전이면
                if(requestedEndTime.isAfter(closingTime)){
                    canUse = false;
                }
            }
        }

        if (!canUse) {
            throw new CatchStudyException(ErrorCode.BOOKING_NOT_AVAILABLE);
        }
    }
}
