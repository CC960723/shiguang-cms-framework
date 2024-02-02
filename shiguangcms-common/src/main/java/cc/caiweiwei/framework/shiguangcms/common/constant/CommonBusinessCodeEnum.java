package cc.caiweiwei.framework.shiguangcms.common.constant;

import cc.caiweiwei.framework.shiguangcms.core.IBusinessCode;

/**
 * 描述：公共业务码
 *
 * @author CaiZhengwei
 * @since 2024/1/1 14:32
 */
public enum CommonBusinessCodeEnum implements IBusinessCode {
    SUCCESS_CODE(IBusinessCode.SUCCESS),
    BUSINESS_EXCEPTION_CODE("1001-0"),
    BUSINESS_EXCEPTION_CODE_WITH_DESCRIPTION("1001-1"),
    DATA_NOT_FOUND_EXCEPTION_CODE("1002-0"),
    DATA_NOT_FOUND_EXCEPTION_CODE_WITH_DESCRIPTION("1002-1"),
    ILLEGAL_PARAM_EXCEPTION_CODE("1003-0"),
    ILLEGAL_PARAM_EXCEPTION_CODE_WITH_DESCRIPTION("1003-1"),
    DATA_EXISTS_EXCEPTION_CODE("1004-0"),
    DATA_EXISTS_EXCEPTION_CODE_WITH_DESCRIPTION("1004-1"),
    DATA_NOT_MATCH_EXCEPTION_CODE("1005-0"),
    DATA_NOT_MATCH_EXCEPTION_CODE_WITH_DESCRIPTION("1005-1"),
    ILLEGAL_ACCESS_EXCEPTION_CODE("1006-0"),
    ILLEGAL_ACCESS_EXCEPTION_CODE_WITH_DESCRIPTION("1006-1"),
    UNKNOWN_EXCEPTION_CODE("1007-0"),
    UNKNOWN_EXCEPTION_CODE_WITH_DESCRIPTION("1007-1"),
    NULL_POINTER_EXCEPTION_CODE("1008-0"),
    NULL_POINTER_EXCEPTION_CODE_WITH_DESCRIPTION("1008-1"),
    HTTP_SEND_FAIL_EXCEPTION_CODE("1009-0"),
    HTTP_SEND_FAIL_EXCEPTION_CODE_WITH_DESCRIPTION("1009-1"),
    UNAUTHORIZED_EXCEPTION_CODE("1010-0");

    private final String businessCode;

    CommonBusinessCodeEnum(String businessCode) {
        this.businessCode = businessCode;
    }

    @Override
    public String getBusinessCode() {
        return businessCode;
    }
}
