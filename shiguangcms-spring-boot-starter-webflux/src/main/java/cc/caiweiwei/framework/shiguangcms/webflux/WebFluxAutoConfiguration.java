package cc.caiweiwei.framework.shiguangcms.webflux;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 描述：WebFlux自动配置类
 *
 * @author CaiZhengwei
 * @since 2024/1/2 16:54
 */
@ComponentScan(basePackageClasses = {WebFluxAutoConfiguration.class})
@AutoConfiguration
public class WebFluxAutoConfiguration {
}
