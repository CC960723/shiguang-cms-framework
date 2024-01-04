package cc.caiweiwei.framework.shiguangcms.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述：排序字段
 *
 * @author CaiZhengwei
 * @since 2023/12/31 16:34
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SortFields {

    /**
     * 数据库表字段名
     *
     * @return 表字段名称
     */
    String[] value() default {};
}
