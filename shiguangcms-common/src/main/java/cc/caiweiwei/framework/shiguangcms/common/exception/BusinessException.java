package cc.caiweiwei.framework.shiguangcms.common.exception;

import cc.caiweiwei.framework.shiguangcms.common.constant.CommonBusinessCodeEnum;

/**
 * 描述：业务异常
 *
 * @author CaiZhengwei
 * @since 2023/12/31 18:32
 */
public class BusinessException extends BaseException {

    public BusinessException() {
        super(CommonBusinessCodeEnum.BUSINESS_EXCEPTION_CODE, null);
    }

    public BusinessException(String message) {
        super(CommonBusinessCodeEnum.BUSINESS_EXCEPTION_CODE_WITH_DESCRIPTION, message);
    }

    public BusinessException(Throwable throwable) {
        super(CommonBusinessCodeEnum.BUSINESS_EXCEPTION_CODE_WITH_DESCRIPTION, throwable.getMessage());
    }

}
