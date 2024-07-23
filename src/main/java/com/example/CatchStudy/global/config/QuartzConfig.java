package com.example.CatchStudy.global.config;

import com.example.CatchStudy.global.Job.QuartzJobFactory;
import com.example.CatchStudy.global.Job.QuartzJobListener;
import com.example.CatchStudy.global.Job.QuartzTriggerListener;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

    private final QuartzJobListener quartzJobListener;

    private final QuartzTriggerListener quartzTriggerListener;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, PlatformTransactionManager transactionManager, ApplicationContext applicationContext) {

        QuartzJobFactory quartzJobFactory = new QuartzJobFactory();
        quartzJobFactory.setApplicationContext(applicationContext);
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        schedulerFactoryBean.setJobFactory(quartzJobFactory);
        schedulerFactoryBean.setApplicationContext(applicationContext);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setTransactionManager(transactionManager);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setGlobalJobListeners(quartzJobListener);
        schedulerFactoryBean.setGlobalTriggerListeners(quartzTriggerListener);

        return schedulerFactoryBean;
    }

    private Properties quartzProperties() {

        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        Properties properties = null;

        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();
        } catch (IOException e) {
            throw new CatchStudyException(ErrorCode.QUARTZ_SCHEDULER_ERROR);
        }

        return properties;
    }

    @Bean
    public Scheduler scheduler(SchedulerFactoryBean factory) throws SchedulerException {
        return factory.getScheduler();
    }
}
