package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.example.demo.controller..*(..))")
    public void logRequest(JoinPoint joinPoint) {
        logger.info("Request to: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* com.example.demo.controller..*(..))", returning = "result")
    public void logResponse(JoinPoint joinPoint, Object result) {
        logger.info("Response from: {} with result: {}", joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(pointcut = "execution(* com.example.demo.controller..*(..))", throwing = "error")
    public void logError(JoinPoint joinPoint, Throwable error) {
        logger.error("Error in method: {} with message: {}", joinPoint.getSignature().getName(), error.getMessage());
    }
}
