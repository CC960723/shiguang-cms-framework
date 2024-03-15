package cc.caiweiwei.framework.shiguangcms.webmvc.config;

import cc.caiweiwei.framework.shiguangcms.common.utils.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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
     * 扩展json消息转换映射关系，除了content-type为application/json外，还支持application/octet-stream的字节流json数据转换
     *
     * @param objectMapper 对象映射
     * @return json数据转换器
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        //除了支持JSON格式的字符串外，还支持字节流的json数据
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        return mappingJackson2HttpMessageConverter;
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
