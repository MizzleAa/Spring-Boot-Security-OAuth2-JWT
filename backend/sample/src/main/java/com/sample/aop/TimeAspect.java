package com.sample.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class TimeAspect {

    @Around("execution(* com.sample.controller.*.*(..))")
    public Object timerController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable, Exception {
        
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        log.info("{}.{} = {}ms", proceedingJoinPoint.getSignature().getDeclaringTypeName(),proceedingJoinPoint.getSignature().getName() , totalTime);
        return result;
    }

}
