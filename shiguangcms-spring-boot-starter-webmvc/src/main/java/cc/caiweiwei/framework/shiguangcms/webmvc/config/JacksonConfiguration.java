package cc.caiweiwei.framework.shiguangcms.webmvc.config;

import cc.caiweiwei.framework.shiguangcms.common.utils.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 描述：jackson配置类
 *
 * @author CaiZhengwei
 * @since 2024/1/2 17:16
 */
@Configuration(proxyBeanMethods = false)
public class JacksonConfiguration {

    @Bean
    @Primary
    public ObjectMapper getObjectMapper() {
        return JsonUtil.OBJECT_MAPPER;
    }

    /**
     * 描述:LocalDateTime转换器，用于转换RequestParam和PathVariable参数
     * 接收毫秒级时间戳数字——>LocalDateTime
     */
    @Bean
    public Converter<Long, LocalDateTime> longToLocalDateTimeConverter() {
        return new LongToLocalDateTimeConverter();
    }

    /**
     * 描述:LocalDateTime转换器，用于转换RequestParam和PathVariable参数
     * 接收毫秒级时间戳字符串——>LocalDateTime
     */
    @Bean
    public Converter<String, LocalDateTime> stringToLocalDateTimeConverter() {
        return new StringToLocalDateTimeConverter();
    }

    private static class LongToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {
        @Override
        public LocalDateTime convert(@Nonnull Long source) {
            return Instant.ofEpochMilli(source).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

    }


    private static class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(@Nonnull String source) {
            long timestamp = Long.parseLong(source);
            return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
    }
}
