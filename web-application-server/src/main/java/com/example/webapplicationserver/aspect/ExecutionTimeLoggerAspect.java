package com.example.webapplicationserver.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExecutionTimeLoggerAspect {

    @Around("execution(* com.example.webapplicationserver.utils.ImageProcessUtils.*(..))")
    public Object logImageProcessUtils(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint);
    }

    @Around("execution(* com.example.webapplicationserver.utils.S3Utils.*(..))")
    public Object logS3UtilsMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint);
    }

    @Around("execution(* com.example.webapplicationserver.service..*(..))") // All methods in the service package
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // method execution
        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        String methodName = joinPoint.getSignature().toShortString();
        log.info("{} | {} ms", methodName, executionTime);

        return result;
    }
}
