package ru.education.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class BeforeAfterWebServiceLogger {

    private final static Logger LOG = LoggerFactory.getLogger(BeforeAfterWebServiceLogger.class);

    @Pointcut("@annotation(ru.education.annotation.BeforeLoggable)")
    public void beforeLoggableMethod() { }

    @Pointcut("@annotation(ru.education.annotation.AfterLoggable)")
    public void afterLoggableMethod() { }

    @Before(value = "beforeLoggableMethod()")
    public void logBeforeWebServiceCall(JoinPoint thisJoinPoint) throws Throwable {
        String methodName = thisJoinPoint.getSignature().getName();
        Object[] methodArgs = thisJoinPoint.getArgs();

        LOG.info("Call method " + methodName + " with args " + Arrays.toString(methodArgs));

    }

    @After(value = "afterLoggableMethod()")
    public void logAfterWebServiceCall(JoinPoint thisJoinPoint) throws Throwable {
        String methodName = thisJoinPoint.getSignature().getName();
        Object[] methodArgs = thisJoinPoint.getArgs();

        LOG.info("Call method " + methodName + " with args " + Arrays.toString(methodArgs));

    }
}
