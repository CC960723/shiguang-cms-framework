package cc.caiweiwei.framework.shiguangcms.webmvc;

import cc.caiweiwei.framework.shiguangcms.common.utils.SnowflakeIdUtil;
import cc.caiweiwei.framework.shiguangcms.webmvc.servletcomponent.ServletComponentPackageBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import java.net.InetAddress;

/**
 * 描述：WebMVC自动配置类
 *
 * @author CaiZhengwei
 * @since 2024/1/2 16:54
 */
@ComponentScan(basePackageClasses = {WebMvcAutoConfiguration.class})
@ServletComponentScan(basePackageClasses = {ServletComponentPackageBase.class})
@AutoConfiguration
public class WebMvcAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(WebMvcAutoConfiguration.class);

    @Bean(name = "snowflakeIdGenerator")
    @ConditionalOnMissingBean(SnowflakeIdUtil.SnowflakeIdGenerator.class)
    public SnowflakeIdUtil.SnowflakeIdGenerator snowflakeIdGenerator(ApplicationContext applicationContext) {
        try {
            Class<?> inetUtilsClass = Class.forName("org.springframework.cloud.commons.util.InetUtils");
            InetUtils inetUtils = (InetUtils) applicationContext.getBean(inetUtilsClass);
            return new SnowflakeIdUtil.SnowflakeIdGenerator(inetUtils.findFirstNonLoopbackAddress());
        } catch (Exception e) {
            logger.warn("不存在SpringCloud的InetUtils工具类");
        }
        return new SnowflakeIdUtil.SnowflakeIdGenerator(InetAddress.getLoopbackAddress());
    }

    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(StringUtils
                .commaDelimitedListToStringArray(StringUtils.trimAllWhitespace("i18n/messages")));
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }


}
