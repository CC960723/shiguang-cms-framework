package cc.caiweiwei.framework.shiguangcms.webmvc.aop;

import cc.caiweiwei.framework.shiguangcms.common.utils.JsonUtil;
import cc.caiweiwei.framework.shiguangcms.core.holder.TokenHolder;
import cc.caiweiwei.framework.shiguangcms.core.holder.TraceIdHolder;
import cc.caiweiwei.framework.shiguangcms.core.holder.UserIdHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * 描述：请求响应日志切面
 *
 * @author CaiZhengwei
 * @since 2024/1/2 21:12
 */
@Aspect
@Component
public class RequestResponseLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLogAspect.class);

    private final ThreadLocal<Long> requestStartTimestamp = new ThreadLocal<>();

    /**
     * 定义请求响应日志切点
     */
    @Pointcut("execution(public * cc.caiweiwei.*.*.query.controller.*.*(..))||execution(public * cc.caiweiwei.*.*.command.controller.*.*(..))")
    public void requestResponseLogPointCut() {

    }


    /**
     * 定义请求日志切面
     *
     * @param joinPoint 连接点
     */
    @Before(value = "requestResponseLogPointCut()")
    public void requestLogAspect(JoinPoint joinPoint) {
        requestStartTimestamp.set(System.currentTimeMillis());
        StringBuilder builder = new StringBuilder();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = servletRequestAttributes.getRequest();
            builder.append("[START|")
                    .append("traceId=").append(TraceIdHolder.getTraceId()).append("|")
                    .append("userId=").append(UserIdHolder.getUserId()).append("|")
                    .append("authentication=").append(TokenHolder.getToken()).append("|")
                    .append("remoteAddr=").append(request.getRemoteAddr()).append("|")
                    .append("requestURI=").append(request.getRequestURI()).append("|")
                    .append("requestURL=").append(request.getRequestURL()).append("|")
                    .append("requestMethod=").append(request.getMethod()).append("]").append("\r\n");
            builder.append("headers=[").append("\r\n");
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headName = headerNames.nextElement();
                builder.append(headName).append(":").append(request.getHeader(headName)).append("\r\n");
            }
            builder.append("]").append("\r\n");
        }

        if (joinPoint.getSignature() instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            builder.append("ClassPath=").append(methodSignature.getDeclaringType().getName()).append(".").append(method.getName()).append("(");
            Parameter[] parameters = method.getParameters();
            int parameterIndex = 0;
            int parameterCount = method.getParameterCount();
            for (Parameter parameter : parameters) {
                builder.append(parameter.getType().getName()).append(" ").append(parameter.getName());
                parameterIndex++;
                if (parameterIndex < parameterCount) {
                    builder.append(", ");
                }
            }
            builder.append(")").append("\r\n");
            builder.append("paramsIn").append(Arrays.toString(joinPoint.getArgs()));
            logger.info("Request Log: {}", builder);
        }
    }

    /**
     * 定义响应日志切面
     *
     * @param result 响应结果
     */
    @AfterReturning(value = "requestResponseLogPointCut()", returning = "result")
    public void responseLogAspect(Object result) {
        long executedTime = System.currentTimeMillis() - requestStartTimestamp.get();
        String resultData = "";
        if (result != null) {
            try {
                resultData = JsonUtil.toJsonStr(resultData);
            } catch (JsonProcessingException e) {
                resultData = "返回值json序列化失败，原因是：" + e.getMessage();
            }
        }
        logger.info("Response Log: [END|traceId={}ms|executedTime={}|result={}]", TraceIdHolder.getTraceId(), executedTime, resultData);
    }


}
