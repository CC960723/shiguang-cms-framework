package cc.caiweiwei.framework.shiguangcms.core.entity.result;

import cc.caiweiwei.framework.shiguangcms.core.IScrollPageSortId;
import cc.caiweiwei.framework.shiguangcms.core.entity.page.ScrollPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 描述：滚动分页结果
 *
 * @author CaiZhengwei
 * @since 2023/12/31 16:35
 */
@ApiModel
public class BaseScrollResult<L extends List<? extends IScrollPageSortId>> extends BaseResult {

    /**
     * 前一个滚动ID
     */
    @ApiModelProperty(value = "前一个滚动ID")
    private Long prevScrollId;

    /**
     * 数据列表
     */
    @ApiModelProperty(value = "数据列表")
    private L data;

    /**
     * 后一个滚动ID
     */
    @ApiModelProperty(value = "后一个滚动ID")
    private Long nextScrollId;

    @SuppressWarnings({"unchecked"})
    public static <L extends List<? extends IScrollPageSortId>> BaseScrollResult<L> successOf(L data, ScrollPage scrollPage) {
        BaseScrollResult<L> result = new BaseScrollResult<>();
        result.setData(data);
        if (data == null || data.isEmpty()) {
            return result;
        }
        List<IScrollPageSortId> list = (List<IScrollPageSortId>) data;
        list.sort((o1, o2) -> {
            if (scrollPage.isScrollSortDesc()) {
                return Long.compare(o2.getScrollSortId().longValue(), o1.getScrollSortId().longValue());
            } else {
                return Long.compare(o1.getScrollSortId().longValue(), o2.getScrollSortId().longValue());
            }
        });
        int size = data.size();
        result.setPrevScrollId(data.get(0).getScrollSortId().longValue());
        result.setNextScrollId(data.get(size - 1).getScrollSortId().longValue());
        return result;
    }

    public Long getPrevScrollId() {
        return prevScrollId;
    }

    public void setPrevScrollId(Long prevScrollId) {
        this.prevScrollId = prevScrollId;
    }

    public L getData() {
        return data;
    }

    public void setData(L data) {
        this.data = data;
    }

    public Long getNextScrollId() {
        return nextScrollId;
    }

    public void setNextScrollId(Long nextScrollId) {
        this.nextScrollId = nextScrollId;
    }

    @Override
    public String toString() {
        return "BaseScrollResult{" +
                "prevScrollId=" + prevScrollId +
                ", data=" + data +
                ", nextScrollId=" + nextScrollId +
                "} " + super.toString();
    }
}
