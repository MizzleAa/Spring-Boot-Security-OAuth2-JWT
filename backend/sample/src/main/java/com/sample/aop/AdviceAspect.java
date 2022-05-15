package com.sample.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class AdviceAspect {
    
    @Around("execution(* com.sample.advice.*.*(..))")
    public Object adviceController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable, Exception {
        log.error("Adivce Error = {}.{}", proceedingJoinPoint.getSignature().getDeclaringTypeName(), proceedingJoinPoint.getSignature().getName());
        Object result = proceedingJoinPoint.proceed();
        return result;
    }
}
