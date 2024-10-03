package com.spring.todo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggerAspect {
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Pointcut("execution(* com.spring.todo.controller.*.*(..))")
    private void forController() {
    }

    @Pointcut("execution(* com.spring.todo.service.*.*(..))")
    private void forService() {
    }

    @Pointcut("execution(* com.spring.todo.advice.*.*(..))")
    private void forAdvice() {
    }

    @Pointcut("execution(* com.spring.todo.repository.*.*(..))")
    private void forRepository() {
    }

    @Before("forController() || forService() || forAdvice() || forRepository()")
    void loggingBefore(JoinPoint joinPoint) {
        logger.info("======> @Before ");
        logger.info("Method: " + joinPoint.getSignature().toShortString());
        logger.info("Method args: " + Arrays.toString(joinPoint.getArgs()));
    }

    @Around("forController()")
    Object executionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = "Error!!!";
        long begin = System.currentTimeMillis();
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            logger.warning("Exception occurred " + e.getMessage());
            // re-throw the exception to caller
            throw e;
        }
        long end = System.currentTimeMillis();
        logger.info("Execution time: " + (end - begin) / 1000 + " seconds");
        return result;
    }

    @AfterReturning(pointcut = "forController() || forService() || forAdvice() || forRepository()", returning = "result")
    Object loggingAfter(JoinPoint joinPoint, Object result) {
        logger.info("======> @AfterReturning from " + joinPoint.getSignature().toShortString());
        logger.info("Return value: " + result);
        return result;
    }
}
