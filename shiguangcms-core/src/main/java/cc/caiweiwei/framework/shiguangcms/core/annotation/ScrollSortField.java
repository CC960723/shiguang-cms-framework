package cc.caiweiwei.framework.shiguangcms.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述：滚动排序字段
 *
 * @author CaiZhengwei
 * @since 2023/12/31 16:34
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ScrollSortField {

    /**
     * 用于滚动排序的字段名，只能是一个字段
     *
     * @return 滚动排序字段
     */
    String value();
}
