package com.example.CatchStudy.global.redisson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributeLock {
    String lockName();

    String identifier();

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    long waitTime() default 0L; //락 획득 대기 시간

    long leaseTime() default 5L; //락 임대 시간
}
