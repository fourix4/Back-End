package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.entity.Booking;
import com.example.CatchStudy.global.enums.BookingStatus;
import com.example.CatchStudy.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final BookingRepository bookingRepository;

    @Transactional
    public void checkAndCancelBooking(Long bookingId) { //예약된 좌석이 결제 30분 후 아직 '입실 전' 상태 일 때 좌석 취소 시킴
        printInfo();
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if (booking != null && booking.getSeat() != null && booking.getStatus().equals(BookingStatus.beforeEnteringRoom)) {
            booking.cancelBeforeEnteringSeat();
            booking.getSeat().updateSeatStatus(true); // 좌석 사용 가능으로 변경
        }
    }


    @Transactional
    public void checkAndCheckInRoomBooking(Long bookingId){ // 예약된 스터디룸이 입실 시간이 되었을 때 '입실 중' 으로 변경
        printInfo();
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if(booking != null && booking.getSeat() == null && booking.getBookedRoomInfo() != null && booking.getStatus().equals(BookingStatus.beforeEnteringRoom)){
            booking.checkInRoomBooking();
        }
    }


    @Transactional
    public void checkAndCheckOutRoomBooking(Long bookingId){ // 입실 중이 스터디룸이 퇴실 시간이 되었을 때 '이용 완료'로 변경
        printInfo();
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if(booking != null && booking.getSeat() == null && booking.getStatus().equals(BookingStatus.enteringRoom)){
            booking.checkOutRoomBooking();
        }
    }

    @Transactional
    public void checkAndCheckOutSeatBooking(Long bookingId){ // 좌석이 퇴실 시간이 되었는데 '입실 중' 일 경우 '이용 완료' 로 변경
        printInfo();
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if(booking != null && booking.getSeat() != null && booking.getStatus().equals(BookingStatus.enteringRoom)){
            booking.checkOutSeatBooking(booking.getEndTime());
            booking.getSeat().updateSeatStatus(true);
        }
    }

    private void printInfo(){
        boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
        log.info("tx active={}",txActive );
    }

}
