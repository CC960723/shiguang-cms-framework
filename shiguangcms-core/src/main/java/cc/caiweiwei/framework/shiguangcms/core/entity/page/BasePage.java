package cc.caiweiwei.framework.shiguangcms.core.entity.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述：基础分页
 *
 * @author CaiZhengwei
 * @since 2023/12/31 16:35
 */
@ApiModel
public class BasePage extends AbstractPage {
    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页（必须是非负整数）", allowEmptyValue = true, example = "1")
    private long currentPage = 1L;

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 获取有效的偏移量页码，如果是负数或者0默认是1,最大查询1000页
     *
     * @return 有效的的页码
     */
    @ApiModelProperty(hidden = true)
    public long getValidCurrentOffsetPage() {
        long validCurrentPage = Math.max(this.currentPage, 1);
        return validCurrentPage > 1000 ? (1000 - 1) * getValidPageSize() : (validCurrentPage - 1) * getValidPageSize();
    }

    /**
     * 获取有效的查询条数，默认最小查询60条，最大查询1000条
     *
     * @return 有效的查询条数
     */
    @ApiModelProperty(hidden = true)
    public long getValidPageSize() {
        long validPageSize = Math.max(this.getPageSize(), 60);
        return validPageSize > 1000 ? 1000 : validPageSize;
    }

    @Override
    public String toString() {
        return "BasePage{" +
                "currentPage=" + currentPage +
                "} " + super.toString();
    }
}
