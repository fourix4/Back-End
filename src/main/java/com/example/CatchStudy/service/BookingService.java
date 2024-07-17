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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
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

    private final ScheduledExecutorService scheduler;

    private final SchedulerService schedulerService;



    @Transactional
    public Long saveBooking(SeatBookingDto dto, Long userId) {
        Users user = usersRepository.findByUserId(userId).orElseThrow(() -> new CatchStudyException(ErrorCode.USER_NOT_FOUND));
        Booking booking = null;
        if (dto.getType() == SeatType.seat) {
            Seat seat = seatRepository.findBySeatIdLock(dto.getSeatId()).orElseThrow(() -> new CatchStudyException(ErrorCode.SEAT_NOT_FOUND));
            if (!seat.getIsAvailable()) {
                throw new CatchStudyException(ErrorCode.SEAT_NOT_AVAILABLE);
            }
            if (bookingRepository.existsBySeatSeatId(seat.getSeatId())) {
                throw new CatchStudyException(ErrorCode.SEAT_NOT_SELECT);
            }

            booking = bookingRepository.save(Booking.of(dto.getTime(), user, seat.getStudyCafe(), seat));

        } else if (dto.getType() == SeatType.room) {
            Room room = roomRepository.findByRoomIdLock(dto.getRoomId()).orElseThrow(() -> new CatchStudyException(ErrorCode.ROOM_NOT_FOUND));

            if (bookedRoomInfoRepository.existsBookedRoom(room.getRoomId(), dto.getStartTime(), dto.getStartTime().plusMinutes(dto.getTime())) != 0) { //해당 날짜 시간에 예약되어있는 룸이 있는지 학인
                throw new CatchStudyException(ErrorCode.ROOM_NOT_AVAILABLE);
            }

            //예약 시작 시간 / 퇴실 시간 저장
            LocalDateTime bookingStartTime = dto.getStartTime();
            Integer time = dto.getTime();
            LocalTime localTime = LocalTime.of(bookingStartTime.getHour(), bookingStartTime.getMinute());
            LocalTime endLocalTime = localTime.plusMinutes(time);
            LocalDate localDate = LocalDate.of(bookingStartTime.getYear(), bookingStartTime.getMonth(), bookingStartTime.getDayOfMonth());
            LocalDateTime bookedEndTime = bookingStartTime.plusMinutes(time);

            BookedRoomInfo bookedRoomInfo = BookedRoomInfo.of(room, localTime, endLocalTime, localDate, bookingStartTime, bookedEndTime);
            bookedRoomInfoRepository.save(bookedRoomInfo);
            booking = bookingRepository.save(Booking.of(user, time, room.getStudyCafe(), bookedRoomInfo, bookingStartTime, bookedEndTime));


        }
        return paymentRepository.save(Payment.of(dto.getPaymentType(), booking, PaymentStatus.ready)).getPaymentId();

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
        List<Payment> payments = paymentRepository.findTop30ByBooking_User_UserIdOrderByPaymentTimeDesc(userId);
        List<BookingHistoryDto> bookingHistoryDtos = payments.stream().map(payment -> {
            Booking booking = payment.getBooking();
            StudyCafe studyCafe = booking.getStudyCafe();
            String cafeImage =  cafeImageRepository.findByStudyCafeCafeIdAndImageType(studyCafe.getCafeId(),ImageType.thumbnail)
                    .map(CafeImage::getCafeImage)
                    .orElse("기본 thumbnail url");

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

        Page<Payment> paymentsPage = paymentRepository.findByBooking_User_UserIdAndPaymentTimeBetween(userId,startTime,endTime,pageable);
        List<BookingHistoryDto> bookingDtos = paymentsPage.getContent().stream().map(payment -> {
            Booking booking = payment.getBooking();
            StudyCafe studyCafe = booking.getStudyCafe();

            String cafeImage = cafeImageRepository.findByStudyCafeCafeIdAndImageType(studyCafe.getCafeId(),ImageType.thumbnail)
                    .map(CafeImage::getCafeImage)
                    .orElse("기본 thumbnail url");

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
        List<Booking> seats = bookingRepository.getAvailableSeats(userId);
        List<Booking> rooms = bookingRepository.getAvailableRooms(userId);
        List<AvailableBookingSeatDto> bookingSeatDtos = seats.stream()
                .map(booking -> {
                    Payment payment = paymentRepository.findByBookingBookingId(booking.getBookingId())
                            .orElseThrow(()-> new CatchStudyException(ErrorCode.PAYMENT_NOT_FOUND));
                    StudyCafe studyCafe = booking.getStudyCafe();

                    return AvailableBookingSeatDto.toDto(
                            booking.getBookingId(),
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

        List<AvailableBookingRoomDto> bookingRoomDtos = rooms.stream()
                .map(booking -> {
                    Payment payment = paymentRepository.findByBookingBookingId(booking.getBookingId())
                            .orElseThrow(()-> new CatchStudyException(ErrorCode.PAYMENT_NOT_FOUND));
                    StudyCafe studyCafe = booking.getStudyCafe();

                    return AvailableBookingRoomDto.toDto(
                            booking.getBookingId(),
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
        checkAndCheckOutSeatBooking(booking.getBookingId(),nowTime.plusMinutes(booking.getTime()));
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

    public void checkAndCheckOutSeatBooking(Long bookingId,LocalDateTime checkOutTime){
        LocalDateTime now = LocalDateTime.now();
        long delay = Duration.between(now,checkOutTime).toMillis();
        scheduler.schedule(()->schedulerService.checkAndCheckOutSeatBooking(bookingId),delay, TimeUnit.MILLISECONDS);
    }

    @Transactional
    public void deleteBooking(Long paymentId) {
        paymentRepository.deleteByPaymentId(paymentId);
    }
}
