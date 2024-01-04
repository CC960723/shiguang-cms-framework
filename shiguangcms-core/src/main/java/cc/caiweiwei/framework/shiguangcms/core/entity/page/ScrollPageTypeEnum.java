package cc.caiweiwei.framework.shiguangcms.core.entity.page;

import cc.caiweiwei.framework.shiguangcms.core.IBaseEnum;
/**
 * 描述：滚动分页类型
 *
 * @author CaiZhengwei
 * @since 2023/12/31 17:48
 */
public enum ScrollPageTypeEnum implements IBaseEnum {

    PREV_SCROLL("向前滚动"),
    NEXT_SCROLL("向后滚动");

    private final String description;

    ScrollPageTypeEnum(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
