package com.example.CatchStudy.global.Job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QuartzTriggerListener implements TriggerListener {

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) { //트리거가 실행될 때 호출
        log.info("Trigger 실행");
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) { //트리거가 실행되지 못했을 때 호출
        log.warn("Trigger misfired: {}", trigger.getKey());
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) { //트리거가 작업을 완료한 후에 호출
        log.info("Trigger complete: {}", trigger.getKey());
    }
}
