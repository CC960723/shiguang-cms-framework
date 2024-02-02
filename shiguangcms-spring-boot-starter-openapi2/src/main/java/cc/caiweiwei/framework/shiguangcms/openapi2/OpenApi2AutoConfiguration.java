package cc.caiweiwei.framework.shiguangcms.openapi2;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;

/**
 * 描述：OpenApi自动配置类
 *
 * @author CaiZhengwei
 * @since 2024/1/2 16:44
 */
@ConditionalOnProperty(value = "knife4j.enable", havingValue = "true")
@ComponentScan(basePackageClasses = {OpenApi2AutoConfiguration.class})
@AutoConfiguration(after = {MessageSource.class})
public class OpenApi2AutoConfiguration {

}
