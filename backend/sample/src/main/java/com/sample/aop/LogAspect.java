package com.sample.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;


@Aspect
@Component
public class LogAspect {

    @Around("execution(* com.sample.controller.*.*(..))")
    public Object ControllerLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable, Exception {
        //log.info("start = {} / {}", proceedingJoinPoint.getSignature().getDeclaringTypeName(), proceedingJoinPoint.getSignature().getName());
        Object result = proceedingJoinPoint.proceed();
        //log.info("end = {} / {}", proceedingJoinPoint.getSignature().getDeclaringTypeName(), proceedingJoinPoint.getSignature().getName());
        return result;
    }
}
