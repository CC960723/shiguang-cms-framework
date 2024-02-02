package cc.caiweiwei.framework.shiguangcms.core.entity.result;

import cc.caiweiwei.framework.shiguangcms.core.entity.page.BasePage;
import cc.caiweiwei.framework.shiguangcms.core.holder.TotalSizeHolder;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 描述：分页结果
 *
 * @author CaiZhengwei
 * @since 2023/12/31 16:35
 */
public class BasePageResult<L extends List<?>> extends BaseResult {

    /**
     * 数据列表
     */
    @ApiModelProperty(value = "数据列表")
    private L data;

    /**
     * 页码
     */
    @ApiModelProperty(value = "页码")
    private long currentPage = 1;

    /**
     * 分页大小
     */
    @ApiModelProperty(value = "分页大小")
    private long pageSize = 60;

    /**
     * 总条数
     */
    @ApiModelProperty(value = "总条数")
    private long totalSize = 0;

    private BasePageResult() {

    }

    public static <L extends List<?>> BasePageResult<L> successOf(L dataList, BasePage basePage) {
        BasePageResult<L> resultPageVo = new BasePageResult<>();
        resultPageVo.setData(dataList);
        if (basePage == null) {
            return resultPageVo;
        }
        if (TotalSizeHolder.getTotalSize() != null) {
            resultPageVo.setTotalSize(TotalSizeHolder.getTotalSize());
            TotalSizeHolder.removeTotalSizeContext();
        }
        resultPageVo.setCurrentPage(Math.max(basePage.getCurrentPage(), 1));
        resultPageVo.setPageSize(basePage.getValidPageSize());
        return resultPageVo;
    }

    public L getData() {
        return data;
    }

    public void setData(L data) {
        this.data = data;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    @Override
    public String toString() {
        return "BasePageResult{" +
                "data=" + data +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", totalSize=" + totalSize +
                "} " + super.toString();
    }
}
