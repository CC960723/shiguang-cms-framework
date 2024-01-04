package cc.caiweiwei.framework.shiguangcms.core;

/**
 * 描述：业务码类型
 *
 * @author CaiZhengwei
 * @since 2023/12/31 16:35
 */
public interface IBusinessCode {
    /**
     * 成功业务码
     */
    String SUCCESS = "0000";

    /**
     * 获取业务码
     *
     * @return 业务码
     */
    String getBusinessCode();
}
