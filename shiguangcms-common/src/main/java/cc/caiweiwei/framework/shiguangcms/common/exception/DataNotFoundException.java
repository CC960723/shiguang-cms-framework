package cc.caiweiwei.framework.shiguangcms.common.exception;

import cc.caiweiwei.framework.shiguangcms.common.constant.CommonBusinessCodeEnum;

/**
 * 描述：数据找不到异常
 *
 * @author CaiZhengwei
 * @since 2023/12/31 18:32
 */
public class DataNotFoundException extends BaseException {

    public DataNotFoundException() {
        super(CommonBusinessCodeEnum.DATA_NOT_FOUND_EXCEPTION_CODE, null);
    }

    public DataNotFoundException(String message) {
        super(CommonBusinessCodeEnum.DATA_NOT_FOUND_EXCEPTION_CODE_WITH_DESCRIPTION, message);
    }

    public DataNotFoundException(Throwable throwable) {
        super(CommonBusinessCodeEnum.DATA_NOT_FOUND_EXCEPTION_CODE_WITH_DESCRIPTION, throwable.getMessage());
    }

}
