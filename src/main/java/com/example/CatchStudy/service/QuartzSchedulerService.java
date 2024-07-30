package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.entity.Payment;
import com.example.CatchStudy.global.Job.CancelBeforeEnteringSeatJob;
import com.example.CatchStudy.global.Job.CheckInRoomBookingJob;
import com.example.CatchStudy.global.Job.CheckOutRoomBookingJob;
import com.example.CatchStudy.global.Job.CheckOutSeatBookingJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuartzSchedulerService {
    private final Scheduler scheduler;

    public void scheduleBookingSeatStatusCheck(Long bookingId, Payment payment) throws SchedulerException { //좌석 결제 30분 후 입실 상태 확인

        JobDetail jobDetail = JobBuilder.newJob(CancelBeforeEnteringSeatJob.class)
                .withIdentity("cancelBeforeEnteringSeatJob_" + bookingId, "cancelBeforeEnteringSeatGroup")
                .usingJobData("bookingId", bookingId)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("cancelBeforeEnteringSeatJob_" + bookingId, "cancelBeforeEnteringSeatGroup")
                .startAt(Date.from(payment.getPaymentTime().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void scheduleRoomEnterUpdate(Long bookingId, LocalDateTime checkInTime) throws SchedulerException {//스터디룸 입실 시간 되면 booking 테이블 '입실 중' 변경

        JobDetail jobDetail = JobBuilder.newJob(CheckInRoomBookingJob.class)
                .withIdentity("checkInRoomBookingJob_" + bookingId, "checkInRoomBookingGroup")
                .usingJobData("bookingId", bookingId)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("checkInRoomBookingJob_" + bookingId, "checkInRoomBookingGroup")
                .startAt(Date.from(checkInTime.atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void scheduleCheckOutRoomBooking(Long bookingId, LocalDateTime checkOutTime) throws SchedulerException { //스터디룸 퇴실 시간이 되면 booking 테이블 '이용 완료' 변경

        JobDetail jobDetail = JobBuilder.newJob(CheckOutRoomBookingJob.class)
                .withIdentity("checkOutRoomBooking_" + bookingId, "checkOutRoomBookingGroup")
                .usingJobData("bookingId", bookingId)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("checkOutRoomBooking_" + bookingId, "checkOutRoomBookingGroup")
                .startAt(Date.from(checkOutTime.atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void scheduleCheckOutSeatBooking(Long bookingId, LocalDateTime checkOutTime) throws SchedulerException { //스터디룸 퇴실 시간이 되면 booking 테이블 '이용 완료' 변경

        JobDetail jobDetail = JobBuilder.newJob(CheckOutSeatBookingJob.class)
                .withIdentity("checkOutSeatBooking_" + bookingId, "checkOutSeatBookingGroup")
                .usingJobData("bookingId", bookingId)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("checkOutSeatBooking_" + bookingId, "checkOutSeatBookingGroup")
                .startAt(Date.from(checkOutTime.atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}
