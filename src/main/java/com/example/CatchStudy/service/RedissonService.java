package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.dto.SeatBookingDto;
import com.example.CatchStudy.domain.entity.*;
import com.example.CatchStudy.global.enums.PaymentStatus;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.global.redisson.DistributeLock;
import com.example.CatchStudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class RedissonService {

    private final RedissonClient redissonClient;
    private final RoomRepository roomRepository;
    private final BookedRoomInfoRepository bookedRoomInfoRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final SeatRepository seatRepository;


    @DistributeLock(lockName = "roomLock",identifier = "roomId")
    public Payment saveRoomBooking(SeatBookingDto dto, Long roomId, Users user){
        Room room = roomRepository.findByRoomId(dto.getRoomId()).orElseThrow(() -> new CatchStudyException(ErrorCode.ROOM_NOT_FOUND));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(dto.getStartTime(), formatter);
        if(bookedRoomInfoRepository.existsBookedRoom(room.getRoomId(),localDateTime,localDateTime.plusMinutes(dto.getTime()))!=0){ //해당 날짜 시간에 예약되어있는 룸이 있는지 학인
            throw new CatchStudyException(ErrorCode.BOOKING_NOT_AVAILABLE);
        }
        //예약 시작 시간 / 퇴실 시간 저장
        LocalDateTime bookingStartTime = localDateTime;
        Integer time = dto.getTime();
        LocalTime localTime = LocalTime.of(bookingStartTime.getHour(),bookingStartTime.getMinute());
        LocalTime endLocalTime = localTime.plusMinutes(time);
        LocalDate localDate = LocalDate.of(bookingStartTime.getYear(),bookingStartTime.getMonth(),bookingStartTime.getDayOfMonth());
        LocalDateTime bookedEndTime = bookingStartTime.plusMinutes(time);

        BookedRoomInfo bookedRoomInfo = BookedRoomInfo.of(
                room,
                localTime,
                endLocalTime,
                localDate,
                bookingStartTime,
                bookedEndTime
        );
        bookedRoomInfoRepository.save(bookedRoomInfo);
        Booking booking = bookingRepository.save(Booking.of(user,time,room.getStudyCafe(),bookedRoomInfo,bookingStartTime,bookedEndTime));
        return paymentRepository.save(Payment.of(dto.getPaymentType(), booking, PaymentStatus.ready));
    }

    @DistributeLock(lockName = "seatLock",identifier = "seatId")
    public Payment saveSeatBooking(SeatBookingDto dto, Long seatId, Users user,StudyCafe studyCafe){
        Seat seat = seatRepository.findBySeatId(dto.getSeatId()).orElseThrow(() -> new CatchStudyException(ErrorCode.SEAT_NOT_FOUND));
        if(!seat.getIsAvailable()){
            throw new CatchStudyException(ErrorCode.BOOKING_NOT_AVAILABLE);
        }
        seat.updateSeatStatus(false);
        Booking booking = bookingRepository.save(Booking.of(dto.getTime(), user, studyCafe, seat));
        return paymentRepository.save(Payment.of(dto.getPaymentType(), booking, PaymentStatus.ready));
    }
}
