package cc.caiweiwei.framework.shiguangcms.redis;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 描述：Redis自动配置类
 *
 * @author CaiZhengwei
 * @since 2024/1/2 16:54
 */
@ComponentScan(basePackageClasses = {RedisAutoConfiguration.class})
@AutoConfiguration
public class RedisAutoConfiguration {
}
