package com.example.librarymanagementsystem.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.librarymanagementsystem.servicImpl.BookServiceImpl.*(..)) || execution(* com.example.librarymanagementsystem.servicImpl.BorrowingRecordServiceImpl.*(..)) || execution(* com.example.librarymanagementsystem.servicImpl.PatronServiceImpl.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object proceed = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            logger.info("Method {} executed in {} ms", joinPoint.getSignature(), executionTime);
            return proceed;
        } catch (Throwable throwable) {
            logger.error("Error in method {}", joinPoint.getSignature(), throwable);
            throw throwable;
        }
    }
}