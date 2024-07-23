package com.example.CatchStudy.global.Job;

import com.example.CatchStudy.domain.entity.Booking;
import com.example.CatchStudy.domain.entity.Seat;
import com.example.CatchStudy.global.enums.BookingStatus;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.repository.BookingRepository;
import com.example.CatchStudy.repository.SeatRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@NoArgsConstructor
@Slf4j
public class CancelBeforeEnteringSeatJob implements Job {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException { //예약된 좌석이 결제 30분 후 아직 '입실 전' 상태 일 때 좌석 취소 시킴
        Long bookingId = context.getJobDetail().getJobDataMap().getLong("bookingId");

        try {
            transactionTemplate.execute(status -> {
                Booking booking = bookingRepository.findByBookingId(bookingId);

                if (booking != null && booking.getSeat() != null && booking.getStatus().equals(BookingStatus.beforeEnteringRoom)) {
                    Seat seat = seatRepository.findBySeatId(booking.getSeat().getSeatId()).orElse(null);

                    if (seat != null) {
                        booking.cancelBeforeEnteringSeat();
                        seat.updateSeatStatus(true); // 좌석 사용 가능으로 변경
                    }

                }
                return null;
            });

        } catch (Exception e) {
            log.error("Job execution resulted in exception: ", e);
            throw new CatchStudyException(ErrorCode.QUARTZ_SCHEDULER_ERROR);
        }
    }
}
