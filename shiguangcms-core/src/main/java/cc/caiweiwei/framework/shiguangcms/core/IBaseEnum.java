package cc.caiweiwei.framework.shiguangcms.core;

/**
 * 描述：枚举类型
 *
 * @author CaiZhengwei
 * @since 2023/12/31 16:35
 */
public interface IBaseEnum {

    /**
     * 获取枚举值描述信息
     *
     * @return 枚举值描述信息
     */
    default String getDescription() {
        return toString();
    }
}
