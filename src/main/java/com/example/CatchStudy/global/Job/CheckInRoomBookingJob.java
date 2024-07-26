package com.example.CatchStudy.global.Job;

import com.example.CatchStudy.domain.entity.Booking;
import com.example.CatchStudy.domain.entity.Seat;
import com.example.CatchStudy.global.enums.BookingStatus;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.repository.BookingRepository;
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
public class CheckInRoomBookingJob implements Job {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long bookingId = context.getJobDetail().getJobDataMap().getLong("bookingId");

        try {

            transactionTemplate.execute(status -> {
                Booking booking = bookingRepository.findByBookingId(bookingId);

                if (booking != null && booking.getSeat() == null && booking.getBookedRoomInfo() != null && booking.getStatus().equals(BookingStatus.beforeEnteringRoom)) {
                    booking.checkInRoomBooking();
                }
                return null;
            });

        } catch (Exception e) {
            log.error("Job execution execute resulted in exception: ", e);
            throw new CatchStudyException(ErrorCode.QUARTZ_SCHEDULER_ERROR);
        }
    }
}
