package cc.caiweiwei.framework.shiguangcms.common.exception;

import cc.caiweiwei.framework.shiguangcms.common.constant.CommonBusinessCodeEnum;

public class DataExistsException extends BaseException {

    public DataExistsException() {
        super(CommonBusinessCodeEnum.DATA_EXISTS_EXCEPTION_CODE, null);
    }

    public DataExistsException(String message) {
        super(CommonBusinessCodeEnum.DATA_EXISTS_EXCEPTION_CODE_WITH_DESCRIPTION, message);
    }

    public DataExistsException(Throwable throwable) {
        super(CommonBusinessCodeEnum.DATA_EXISTS_EXCEPTION_CODE_WITH_DESCRIPTION, throwable.getMessage());
    }

}
