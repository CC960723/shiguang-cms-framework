package cc.caiweiwei.framework.shiguangcms.core.entity.page;

import cc.caiweiwei.framework.shiguangcms.core.annotation.ScrollSortField;
import cc.caiweiwei.framework.shiguangcms.core.annotation.SortFields;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 描述：
 *
 * @author CaiZhengwei
 * @since 2023/12/31 16:35
 */
@ApiModel
public class AbstractPage {

    private static final Pattern ILLEGAL_SORT_FIELD_PATTERN = Pattern.compile("^(select|insert|delete|update|from|where|and|or)$");

    /**
     * 每页记录数，默认记录60条数据
     */
    @ApiModelProperty(value = "每页记录数，默认最小值60条数据", allowEmptyValue = true)
    private long pageSize = 60L;

    /**
     * 排序字段数组<br>
     * 注意：滚动排序下，此排序字段数组无效
     */
    @Valid
    @ApiModelProperty(value = "排序字段数组(滚动排序下，此排序字段数组无效!!!)", allowEmptyValue = true)
    private List<SortItem> sorts;

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public List<SortItem> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortItem> sorts) {
        this.sorts = sorts;
    }

    public boolean checkSortFieldsValid() {
        if (sorts == null || sorts.isEmpty()) {
            return true;
        }
        List<String> allowSortFields = obtainSortFields();
        if (allowSortFields == null || allowSortFields.isEmpty()) {
            return false;
        }
        Set<String> sortColumns = sorts.stream().map(SortItem::getColumn).collect(Collectors.toSet());
        for (String sortColumn : sortColumns) {
            boolean allowSort = false;
            for (String allowSortField : allowSortFields) {
                if (allowSortField.equals(sortColumn)) {
                    allowSort = true;
                    break;
                }
            }
            if (!allowSort) {
                return false;
            }
        }
        return true;
    }

    protected final List<String> obtainSortFields() {
        SortFields sortFields = this.getClass().getAnnotation(SortFields.class);
        if (sortFields == null) {
            return null;
        }
        //校验排序字段是否非法
        List<String> sortFieldList = new ArrayList<>();
        for (String sortField : sortFields.value()) {
            if ("".equals(sortField)) {
                continue;
            }
            if (ILLEGAL_SORT_FIELD_PATTERN.matcher(sortField.toLowerCase()).find()) {
                throw new IllegalArgumentException("无效的排序字段，存在SQL注入风险！无效排序字段内容：" + sortField);
            }
            sortFieldList.add(sortField);
        }
        return sortFieldList;
    }

    protected final String obtainScrollSortField() {
        ScrollSortField scrollSortField = this.getClass().getAnnotation(ScrollSortField.class);
        String fieldName = scrollSortField.value();
        if (ILLEGAL_SORT_FIELD_PATTERN.matcher(fieldName.toLowerCase()).find()) {
            throw new IllegalArgumentException("无效的排序字段，存在SQL注入风险！无效排序字段内容：" + fieldName);
        }
        return fieldName;
    }
}
