package com.example.CatchStudy.global.redisson;

import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributeLockAop {

    private final AopForTransaction aopForTransaction;
    private final RedissonClient redissonClient;

    @Around("@annotation(com.example.CatchStudy.global.redisson.DistributeLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributeLock distributeLock = method.getAnnotation(DistributeLock.class);
        String lockKey = distributeLock.lockName();
        String dynamicKey = generateDynamicKey(signature.getParameterNames(),joinPoint.getArgs(),
                distributeLock.identifier());
        String key = lockKey + ":" + dynamicKey;
        RLock rlock = redissonClient.getLock(key);

        try {
            boolean available = rlock.tryLock(distributeLock.waitTime(), distributeLock.leaseTime(), distributeLock.timeUnit());
            if (!available) {
                log.info("{} - 락 획득 실패",key);
                throw new CatchStudyException(ErrorCode.BOOKING_NOT_AVAILABLE);
            }

            log.info("{} - 락 획득 성공" , key);
            return aopForTransaction.proceed(joinPoint);
        } finally {
            try {
                log.info("{} - 락 반납",key);
                rlock.unlock();
            }catch (IllegalMonitorStateException e){ //leaseTime 이 지난 경우 자동 해제된 후 unlock 시도시 발생
                log.info(e + lockKey + dynamicKey);
            }
        }
    }

    private String generateDynamicKey(String[] parameterNames, Object[] args, String identifier){
        for(int i=0; i<parameterNames.length; i++){
            if(parameterNames[i].equals(identifier)){
                return String.valueOf(args[i]);
            }
        }
        throw new IllegalStateException("해당하는 identifier가 없습니다");
    }
}
