package cc.caiweiwei.framework.shiguangcms.common.exception;

import cc.caiweiwei.framework.shiguangcms.common.constant.CommonBusinessCodeEnum;

/**
 * 描述：数据不匹配异常
 *
 * @author CaiZhengwei
 * @since 2023/12/31 18:33
 */
public class DataNotMatchException extends BaseException {

    public DataNotMatchException() {
        super(CommonBusinessCodeEnum.DATA_NOT_MATCH_EXCEPTION_CODE, null);
    }

    public DataNotMatchException(String message) {
        super(CommonBusinessCodeEnum.DATA_NOT_MATCH_EXCEPTION_CODE_WITH_DESCRIPTION, message);
    }

    public DataNotMatchException(Throwable throwable) {
        super(CommonBusinessCodeEnum.DATA_NOT_MATCH_EXCEPTION_CODE_WITH_DESCRIPTION, throwable.getMessage());
    }

}
