package cc.caiweiwei.framework.shiguangcms.core.entity.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 描述：排序字段
 *
 * @author CaiZhengwei
 * @since 2023/12/31 16:35
 */
@ApiModel
public class SortItem {
    /**
     * 排序列名
     */
    @NotBlank(message = "排序字段名不能为空")
    @ApiModelProperty(value = "排序列名（此列表必须是后端接口运行进行排序的表字段名，否者视为非法排序或非法查询）")
    private String column;

    /**
     * 是否降序，默认是升序
     */
    @ApiModelProperty(value = "是否降序，默认是升序", allowEmptyValue = true, example = "false")
    private boolean desc = false;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "SortItem{" +
                "column='" + column + '\'' +
                ", desc=" + desc +
                '}';
    }
}
