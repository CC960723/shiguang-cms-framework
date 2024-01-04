package cc.caiweiwei.framework.shiguangcms.common.exception;

import cc.caiweiwei.framework.shiguangcms.common.constant.CommonBusinessCodeEnum;

/**
 * 描述：Http发送失败异常
 *
 * @author CaiZhengwei
 * @since 2023/12/31 18:33
 */
public class HttpSendFailException extends BaseException {

    public HttpSendFailException() {
        super(CommonBusinessCodeEnum.HTTP_SEND_FAIL_EXCEPTION_CODE, null);
    }

    public HttpSendFailException(String message) {
        super(CommonBusinessCodeEnum.HTTP_SEND_FAIL_EXCEPTION_CODE_WITH_DESCRIPTION, message);
    }

    public HttpSendFailException(Throwable throwable) {
        super(CommonBusinessCodeEnum.HTTP_SEND_FAIL_EXCEPTION_CODE_WITH_DESCRIPTION, throwable.getMessage());
    }

}
