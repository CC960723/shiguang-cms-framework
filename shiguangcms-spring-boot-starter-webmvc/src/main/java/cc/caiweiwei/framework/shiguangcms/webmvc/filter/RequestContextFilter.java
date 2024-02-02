package cc.caiweiwei.framework.shiguangcms.webmvc.filter;

import cc.caiweiwei.framework.shiguangcms.common.constant.CommonConstant;
import cc.caiweiwei.framework.shiguangcms.common.utils.SnowflakeIdUtil;
import cc.caiweiwei.framework.shiguangcms.core.holder.TokenHolder;
import cc.caiweiwei.framework.shiguangcms.core.holder.TraceIdHolder;
import cc.caiweiwei.framework.shiguangcms.core.holder.UserIdHolder;
import org.slf4j.MDC;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 描述：请求上下文过滤器
 *
 * @author CaiZhengwei
 * @since 2024/1/2 22:20
 */
@WebFilter(filterName = "requestContextFilter", urlPatterns = {"/*"}, initParams = {@WebInitParam(
        name = "ignoreUris",
        value = "/doc.html,/v2/api-docs,/swagger-resources,/favicon.ico"
)})
public class RequestContextFilter extends HttpFilter {

    private static final String IGNORE_URIS_PARAM_NAME = "ignoreUris";

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String swaggerRelationUris = getInitParameter(IGNORE_URIS_PARAM_NAME);
        String[] ignoreUris = swaggerRelationUris.split(",");
        String requestURI = request.getRequestURI();
        for (String ignoreUri : ignoreUris) {
            if (requestURI.equals(ignoreUri) || requestURI.startsWith(ignoreUri)) {
                chain.doFilter(request, response);
                return;
            }
        }
        //设置上下文
        String userId = request.getHeader(CommonConstant.USER_ID_HEADER);
        if (userId != null) {
            UserIdHolder.setUserId(Integer.valueOf(userId));
            MDC.put(CommonConstant.USER_ID_HEADER, UserIdHolder.getUserId().toString());
        }

        String traceId = request.getHeader(CommonConstant.TRACE_ID_HEADER);
        if (traceId != null) {
            TraceIdHolder.setTraceId(Long.parseLong(traceId));
        } else {
            TraceIdHolder.setTraceId(SnowflakeIdUtil.next());
        }
        MDC.put(CommonConstant.TRACE_ID_HEADER, TraceIdHolder.getTraceId().toString());

        String[] authorizationHeaders = CommonConstant.AUTHORIZATION_HEADER.split(",");
        for (String authorizationHeader : authorizationHeaders) {
            String token = request.getHeader(authorizationHeader);
            if (token != null) {
                TokenHolder.setToken(token);
                break;
            }
        }
        chain.doFilter(request, response);
        //清除上下文
        UserIdHolder.removeUserIdContext();
        TraceIdHolder.removeTraceIdContext();
        TokenHolder.removeTokenContext();
    }
}
