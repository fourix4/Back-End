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

    public Long saveBooking(SeatBookingDto dto) {
        Users user = usersRepository.findByUserId(dto.getUserId()).orElseThrow(() -> new CatchStudyException(ErrorCode.USER_NOT_FOUND));
        Booking booking = null;
        if (dto.getType() == SeatType.seat) {
            Seat seat = seatRepository.findBySeatId(dto.getSeatId()).orElseThrow(() -> new CatchStudyException(ErrorCode.SEAT_NOT_FOUND));
            booking = bookingRepository.save(Booking.of(dto.getTime(), user, seat.getStudyCafe(), seat));
        } else if (dto.getType() == SeatType.room) {
            Room room = roomRepository.findByRoomId(dto.getRoomId()).orElseThrow(() -> new CatchStudyException(ErrorCode.ROOM_NOT_FOUND));
            booking = bookingRepository.save(Booking.of(dto.getStartTime(), dto.getEndTime(), user, room.getStudyCafe(), room));
        }
        return paymentRepository.save(Payment.of(dto.getPaymentType(), booking, PaymentStatus.ready)).getPaymentId();
    }

    public void deleteBooking(Long paymentId) {
        paymentRepository.deleteByPaymentId(paymentId);
    }
}
