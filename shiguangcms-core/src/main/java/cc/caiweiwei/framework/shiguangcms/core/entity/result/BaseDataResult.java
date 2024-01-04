package cc.caiweiwei.framework.shiguangcms.core.entity.result;

import io.swagger.annotations.ApiModelProperty;

/**
 * 描述：数据结果
 *
 * @author CaiZhengwei
 * @since 2023/12/31 16:35
 */
public class BaseDataResult<T> extends BaseResult {

    /**
     * 数据
     */
    @ApiModelProperty(value = "数据")
    private T data;

    private BaseDataResult() {

    }

    public static BaseDataResult<String> successOf(String data) {
        BaseDataResult<String> result = new BaseDataResult<>();
        result.setData(data);
        return result;
    }

    public static <T> BaseDataResult<T> successOf(T data) {
        BaseDataResult<T> result = new BaseDataResult<>();
        result.setData(data);
        return result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseDataResult{" +
                "data=" + data +
                "} " + super.toString();
    }
}
