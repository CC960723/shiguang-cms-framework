package cc.caiweiwei.framework.shiguangcms.core.entity.result;

import cc.caiweiwei.framework.shiguangcms.core.IBusinessCode;
import cc.caiweiwei.framework.shiguangcms.core.holder.TraceIdHolder;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述：基础结果
 *
 * @author CaiZhengwei
 * @since 2023/12/31 16:35
 */
public class BaseResult {


    /**
     * 状态码
     */
    @ApiModelProperty(value = "状态码")
    private String code = IBusinessCode.SUCCESS;

    /**
     * 描述信息
     */
    @ApiModelProperty(value = "描述信息")
    private String message = "成功";

    /**
     * 链路ID
     */
    @ApiModelProperty(value = "链路ID")
    private Long traceId;

    public BaseResult() {
        this.traceId = TraceIdHolder.getTraceId();
    }

    public BaseResult(IBusinessCode businessCode, String message) {
        this(businessCode.getBusinessCode(), message, TraceIdHolder.getTraceId());
    }

    public BaseResult(String businessCode, String message) {
        this(businessCode, message, TraceIdHolder.getTraceId());
    }

    public BaseResult(String businessCode, String message, Long traceId) {
        this.code = businessCode;
        this.message = message;
        this.traceId = traceId;
    }

    public static BaseResult successOf() {
        return new BaseResult();
    }

    public static BaseResult successOf(String successMessage) {
        return new BaseResult(IBusinessCode.SUCCESS, successMessage);
    }

    public static BaseResult errorOf(IBusinessCode businessCode, String message) {
        return new BaseResult(businessCode, message);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTraceId() {
        return traceId;
    }

    public BaseResult setTraceId(Long traceId) {
        this.traceId = traceId;
        return this;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", traceId=" + traceId +
                '}';
    }
}
