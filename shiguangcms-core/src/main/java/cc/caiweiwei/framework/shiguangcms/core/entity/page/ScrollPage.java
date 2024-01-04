package cc.caiweiwei.framework.shiguangcms.core.entity.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述：滚动分页
 *
 * @author CaiZhengwei
 * @since 2023/12/31 16:35
 */
@ApiModel
public class ScrollPage extends AbstractPage {

    /**
     * 滚动ID
     */
    @ApiModelProperty(value = "滚动ID（第一次滚动查询时，不需要传递滚动ID）", allowEmptyValue = true)
    private Long scrollId;

    /**
     * 滚动分页类型
     */
    @ApiModelProperty(value = "滚动分页类型（默认向后滚动，支持向前滚动和向后滚动）", allowEmptyValue = true, example = "NEXT_SCROLL")
    private ScrollPageTypeEnum scrollPageType = ScrollPageTypeEnum.NEXT_SCROLL;

    /**
     * 滚动分页排序降序
     * true 降序，false 升序
     */
    @ApiModelProperty(value = "滚动分页数据排序（ture降序数据，false升序数据）", allowEmptyValue = true, example = "false")
    private boolean scrollSortDesc = false;

    public Long getScrollId() {
        return scrollId;
    }

    public void setScrollId(Long scrollId) {
        this.scrollId = scrollId;
    }

    public ScrollPageTypeEnum getScrollPageType() {
        return scrollPageType;
    }

    public void setScrollPageType(ScrollPageTypeEnum scrollPageType) {
        this.scrollPageType = scrollPageType;
    }

    public boolean isScrollSortDesc() {
        return scrollSortDesc;
    }

    public void setScrollSortDesc(boolean scrollSortDesc) {
        this.scrollSortDesc = scrollSortDesc;
    }

    @Override
    public String toString() {
        return "ScrollPage{" +
                "scrollId=" + scrollId +
                ", scrollPageType=" + scrollPageType +
                ", scrollSortDesc=" + scrollSortDesc +
                "} " + super.toString();
    }

}
