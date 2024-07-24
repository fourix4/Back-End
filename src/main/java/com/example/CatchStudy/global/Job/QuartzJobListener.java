package com.example.CatchStudy.global.Job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@Component
public class QuartzJobListener implements JobListener {

    @Override
    public String getName() {
        return "QuartzJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) { //Job이 실행되기 전에 호출
        log.info("Job is about to be executed: {}", context.getJobDetail().getKey());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) { //Job이 중단되었을 때 호출
        log.info("Job execution was vetoed: {}", context.getJobDetail().getKey());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) { //Job이 실행된 후에 호출
        if (!ObjectUtils.isEmpty(jobException)) {
            log.info("Job execution resulted in exception: {}", context.getJobDetail().getKey(), jobException);
        } else {
            log.info("Job was executed successfully: {}", context.getJobDetail().getKey());
        }
    }
}
