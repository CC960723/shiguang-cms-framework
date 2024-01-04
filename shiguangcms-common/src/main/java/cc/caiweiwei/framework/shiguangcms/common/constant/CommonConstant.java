package cc.caiweiwei.framework.shiguangcms.common.constant;

import cc.caiweiwei.framework.shiguangcms.core.IBusinessCode;

/**
 * 描述：公共常量类
 *
 * @author CaiZhengwei
 * @since 2023/12/31 16:57
 */
public interface CommonConstant {

    /**
     * 成功码
     */
    String SUCCESS_CODE = IBusinessCode.SUCCESS;

    /**
     * traceId请求头
     */
    String TRACE_ID_HEADER = "TraceId";

    /**
     * userId请求头
     */
    String USER_ID_HEADER = "UserId";

    /**
     * authentication请求头
     */
    String AUTHORIZATION_HEADER = "Authorization,authorization";

    /**
     * 响应码
     */
    String RESPONSE_CODE = "code";

}
