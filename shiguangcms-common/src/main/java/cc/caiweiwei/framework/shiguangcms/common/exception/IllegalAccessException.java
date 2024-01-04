package cc.caiweiwei.framework.shiguangcms.common.exception;

import cc.caiweiwei.framework.shiguangcms.common.constant.CommonBusinessCodeEnum;

/**
 * 描述：非法访问异常
 *
 * @author CaiZhengwei
 * @since 2023/12/31 18:33
 */
public class IllegalAccessException extends BaseException {

    public IllegalAccessException() {
        super(CommonBusinessCodeEnum.ILLEGAL_ACCESS_EXCEPTION_CODE, null);
    }

    public IllegalAccessException(String message) {
        super(CommonBusinessCodeEnum.ILLEGAL_ACCESS_EXCEPTION_CODE_WITH_DESCRIPTION, message);
    }

    public IllegalAccessException(Throwable throwable) {
        super(CommonBusinessCodeEnum.ILLEGAL_ACCESS_EXCEPTION_CODE_WITH_DESCRIPTION, throwable.getMessage());
    }

}
