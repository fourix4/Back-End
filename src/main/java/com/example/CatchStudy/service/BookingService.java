package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.dto.SeatBookingDto;
import com.example.CatchStudy.domain.dto.response.BookingResponseDto;
import com.example.CatchStudy.domain.entity.*;
import com.example.CatchStudy.global.enums.PaymentStatus;
import com.example.CatchStudy.global.enums.SeatType;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UsersRepository usersRepository;
    private final SeatRepository seatRepository;
    private final PaymentRepository paymentRepository;
    private final RoomRepository roomRepository;
    private final BookedRoomInfoRepository bookedRoomInfoRepository;


    public Long saveBooking(SeatBookingDto dto, Long userId) {
        Users user = usersRepository.findByUserId(userId).orElseThrow(() -> new CatchStudyException(ErrorCode.USER_NOT_FOUND));
        Booking booking = null;
        if (dto.getType() == SeatType.seat) {
            Seat seat = seatRepository.findBySeatIdLock(dto.getSeatId()).orElseThrow(() -> new CatchStudyException(ErrorCode.SEAT_NOT_FOUND));
            if(!seat.getIsAvailable()){
                throw new CatchStudyException(ErrorCode.SEAT_NOT_AVAILABLE);
            }
            if(bookingRepository.existsBySeatSeatId(seat.getSeatId())){
                throw new CatchStudyException(ErrorCode.SEAT_NOT_SELECT);
            }

            booking = bookingRepository.save(Booking.of(dto.getTime(), user, seat.getStudyCafe(), seat));

        } else if (dto.getType() == SeatType.room) {
            Room room = roomRepository.findByRoomIdLock(dto.getRoomId()).orElseThrow(() -> new CatchStudyException(ErrorCode.ROOM_NOT_FOUND));

            if(bookedRoomInfoRepository.existsBookedRoom(room.getRoomId(),dto.getStartTime(),dto.getStartTime().plusMinutes(dto.getTime()))!=0){ //해당 날짜 시간에 예약되어있는 룸이 있는지 학인
                throw new CatchStudyException(ErrorCode.ROOM_NOT_AVILABLE);
            }

            //예약 시작 시간 / 퇴실 시간 저장
            LocalDateTime bookingStartTime = dto.getStartTime();
            Integer time = dto.getTime();
            LocalTime localTime = LocalTime.of(bookingStartTime.getHour(),bookingStartTime.getMinute());
            LocalTime endLocalTime = localTime.plusMinutes(time);
            LocalDate localDate = LocalDate.of(bookingStartTime.getYear(),bookingStartTime.getMonth(),bookingStartTime.getDayOfMonth());
            LocalDateTime bookedEndTime = bookingStartTime.plusMinutes(time);

            BookedRoomInfo bookedRoomInfo = BookedRoomInfo.of(room,localTime,endLocalTime,localDate,bookingStartTime,bookedEndTime);
            bookedRoomInfoRepository.save(bookedRoomInfo);
            booking = bookingRepository.save(Booking.of(user,time,room.getStudyCafe(),bookedRoomInfo,bookingStartTime,bookedEndTime));


        }
        return paymentRepository.save(Payment.of(dto.getPaymentType(), booking, PaymentStatus.ready)).getPaymentId();

    }

    public void deleteBooking(Long paymentId) {
        paymentRepository.deleteByPaymentId(paymentId);
    }
}
