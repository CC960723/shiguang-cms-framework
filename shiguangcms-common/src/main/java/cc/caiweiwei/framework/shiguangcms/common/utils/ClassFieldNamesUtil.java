package cc.caiweiwei.framework.shiguangcms.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：类成员字段名称工具类
 *
 * @author CaiZhengwei
 * @since 2023/12/31 17:59
 */
public class ClassFieldNamesUtil {

    /**
     * 获取类字段属性列表<br>
     * 注意：fieldNames是在类成员中添加的一个用于记录所有其他成员字段的数组，在统计时需要将此字段排除
     * <p>实例代码如下：</p>
     * <pre>
     *     {@code
     *          public class Foo{
     *              //这里的变量名必须是fieldNames如果使用其他的字段名称，将不会进行过滤
     *              private static final List<String> fieldNames;
     *              private String name;
     *              private boolean flag;
     *              private Object other;
     *
     *              //省略其他逻辑方法和getter/setter方法
     *              ……
     *
     *              static {
     *                  fieldNames = ClassFieldNamesUtil.obtainClassFieldNames(Foo.class);
     *              }
     *
     *              public static List<String> obtainAllFieldName() {
     *                  return fieldNames;
     *              }
     *          }
     *     }
     * </pre>
     *
     * @param target 目标Class
     * @return 目标Class成员字段，以及父类成员字段列表
     */
    public static List<String> obtainClassFieldNames(Class<?> target) {
        if (target == null) {
            return null;
        }
        List<String> fieldNames = new ArrayList<>();
        Field[] declaredFields = target.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if ("fieldNames".equals(declaredField.getName())) {
                continue;
            }
            fieldNames.add(declaredField.getName());
        }

        List<Class<?>> superClasses = obtainSuperClasses(target);

        //super
        for (Class<?> superclassItem : superClasses) {
            for (Field superClassDeclaredField : superclassItem.getDeclaredFields()) {
                if ("fieldNames".equals(superClassDeclaredField.getName())) {
                    continue;
                }
                fieldNames.add(superClassDeclaredField.getName());
            }
        }
        return fieldNames;
    }

    public static Field obtainObjectField(Class<?> target, String fieldName) {
        if (target == null || fieldName == null || "".equals(fieldName)) {
            return null;
        }
        Field[] declaredFields = target.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getName().equals(fieldName)) {
                return declaredField;
            }
        }
        //获取下父类中是否有此字段
        List<Class<?>> superClasses = obtainSuperClasses(target);
        //super
        for (Class<?> superclassItem : superClasses) {
            for (Field superClassDeclaredField : superclassItem.getDeclaredFields()) {
                if (superClassDeclaredField.getName().equals(fieldName)) {
                    return superClassDeclaredField;
                }
            }
        }
        return null;
    }

    private static List<Class<?>> obtainSuperClasses(Class<?> target) {
        Class<?> superclass;
        List<Class<?>> superClasses = new ArrayList<>();
        do {
            superclass = target.getSuperclass();
            target = superclass;
            if (superclass != null && !Object.class.getName().equals(superclass.getName())) {
                superClasses.add(superclass);
            }
        } while (superclass != null);
        return superClasses;
    }
}
