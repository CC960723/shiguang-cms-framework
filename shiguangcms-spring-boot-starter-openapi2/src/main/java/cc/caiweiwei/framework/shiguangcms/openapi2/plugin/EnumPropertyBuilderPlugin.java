package cc.caiweiwei.framework.shiguangcms.openapi2.plugin;

import cc.caiweiwei.framework.shiguangcms.core.IBaseEnum;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static springfox.documentation.schema.Annotations.findPropertyAnnotation;
import static springfox.documentation.swagger.schema.ApiModelProperties.findApiModePropertyAnnotation;

/**
 * 描述：Swagger 枚举值扩展插件
 *
 * @author CaiZhengwei
 * @since 2024/1/2 16:41
 */
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class EnumPropertyBuilderPlugin implements ModelPropertyBuilderPlugin {

    @Override
    public void apply(ModelPropertyContext context) {
        Optional<ApiModelProperty> annotation = empty();

        if (context.getAnnotatedElement().isPresent()) {
            annotation = annotation.map(Optional::of).orElse(findApiModePropertyAnnotation(context.getAnnotatedElement().get()));
        }
        if (context.getBeanPropertyDefinition().isPresent()) {
            annotation = annotation.map(Optional::of).orElse(findPropertyAnnotation(context.getBeanPropertyDefinition().get(), ApiModelProperty.class));
        }
        if (!annotation.isPresent()) {
            return;
        }

        Optional<BeanPropertyDefinition> beanPropertyDefinitionOptional = context.getBeanPropertyDefinition();

        if (!beanPropertyDefinitionOptional.isPresent()) {
            return;

        }

        BeanPropertyDefinition beanPropertyDefinition = beanPropertyDefinitionOptional.get();
        AnnotatedField field = beanPropertyDefinition.getField();
        Field annotated = field.getAnnotated();
        Class<?> type = annotated.getType();

        if (!type.isEnum()) {
            return;
        }

        boolean assignableFrom = IBaseEnum.class.isAssignableFrom(type);
        Object[] enumConstants = type.getEnumConstants();
        if (enumConstants == null) {
            return;
        }

        List<String> allowableValuesList = new ArrayList<>();
        if (assignableFrom) {
            for (Object item : enumConstants) {
                IBaseEnum baseEnum = (IBaseEnum) item;
                allowableValuesList.add(baseEnum.toString() + "(PS.=" + baseEnum.getDescription() + ")");
            }
        } else {
            for (Object item : enumConstants) {
                Enum<?> enumItem = (Enum<?>) item;
                allowableValuesList.add(enumItem.toString());
            }
        }

        AllowableListValues allowableListValues = new AllowableListValues(allowableValuesList, "LIST");
        context.getBuilder().allowableValues(allowableListValues);
    }

    @Override
    public boolean supports(@Nonnull DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
