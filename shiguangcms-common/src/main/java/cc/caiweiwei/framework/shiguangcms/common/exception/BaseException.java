package cc.caiweiwei.framework.shiguangcms.common.exception;

import cc.caiweiwei.framework.shiguangcms.common.utils.BusinessCodeMessageUtil;
import cc.caiweiwei.framework.shiguangcms.core.IBusinessCode;

/**
 * 描述：基础运行时异常
 *
 * @author CaiZhengwei
 * @since 2024/1/1 14:07
 */
public abstract class BaseException extends RuntimeException {

    private final IBusinessCode businessCode;


    public BaseException(IBusinessCode businessCode, String message) {
        super(BusinessCodeMessageUtil.getBusinessCodeMessage(businessCode, message));
        if (businessCode == null) {
            throw new IllegalArgumentException("业务码不能为空！");
        }
        this.businessCode = businessCode;
    }

    public IBusinessCode getBusinessCode() {
        return businessCode;
    }


}
