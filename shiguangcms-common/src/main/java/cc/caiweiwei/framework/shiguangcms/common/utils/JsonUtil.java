package cc.caiweiwei.framework.shiguangcms.common.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述：Json工具类
 *
 * @author CaiZhengwei
 * @since 2023/12/31 17:59
 */
public class JsonUtil {

    /**
     * ObjectMapper是线程安全的，可以共享使用
     */
    public static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        SimpleModule longModule = new SimpleModule();
        longModule.addSerializer(Long.class, ToStringSerializer.instance);
        longModule.addDeserializer(Long.class, new LongDeserializer());

        OBJECT_MAPPER.registerModule(javaTimeModule);
        OBJECT_MAPPER.registerModule(longModule);
        OBJECT_MAPPER.getSerializerProvider().setNullValueSerializer(new NullValueJsonSerializer());

        OBJECT_MAPPER.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String value = jsonParser.getValueAsString();
            if (value == null || "".equals(value)) {
                return null;
            }

            long timestamp = Long.parseLong(value);
            Instant instant = Instant.ofEpochMilli(timestamp);
            ZoneId zone = ZoneId.systemDefault();

            return LocalDateTime.ofInstant(instant, zone);
        }
    }

    private static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

            Long timestamp = localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
            jsonGenerator.writeString(String.valueOf(timestamp));
        }
    }

    //Long反序列化字符串
    private static class LongDeserializer extends JsonDeserializer<Long> {
        @Override
        public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

            String value = jsonParser.getValueAsString();
            if (value == null || "".equals(value)) {
                return null;
            }
            return Long.valueOf(value);
        }
    }

    private static class NullValueJsonSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            String fieldName = gen.getOutputContext().getCurrentName();
            Field field = ClassFieldNamesUtil.obtainObjectField(gen.getCurrentValue().getClass(), fieldName);
            if (field == null) {
                gen.writeNull();
                return;
            }
            if (String.class.isAssignableFrom(field.getType())) {
                //设置成空字符串""
                gen.writeString("");
                return;
            }
            if (Collection.class.isAssignableFrom(field.getType()) || field.getType().isArray()) {
                //设置成空数组[]
                gen.writeStartArray();
                gen.writeEndArray();
                return;
            }
            if (Map.class.isAssignableFrom(field.getType())) {
                //设置成空对象{}
                gen.writeStartObject();
                gen.writeEndObject();
                return;
            }
            //引用类型设置成Null
            if (Object.class.isAssignableFrom(field.getType())) {
                gen.writeNull();
            }
        }

    }

    public static String toJsonStr(Object value) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(value);
    }

    /**
     * 解析Map数据
     *
     * @param content   json字符串
     * @param keyType   Map-Key泛型数据类型的Class
     * @param valueType Map-Value泛型数据类型的Class
     * @param <K>       Key数据类型
     * @param <V>       Value数据类型
     * @return 返回指定k, v的Map
     * @throws JsonProcessingException 解析异常
     */
    public static <K, V> Map<K, V> parseJsonStringToMap(String content, Class<K> keyType, Class<V> valueType) throws JsonProcessingException {
        checkContent(content);
        MapLikeType mapLikeType = TypeFactory.defaultInstance().constructMapLikeType(HashMap.class, keyType, valueType);
        return OBJECT_MAPPER.readValue(content, mapLikeType);
    }

    /**
     * 解析List数据
     *
     * @param content json字符串
     * @param type    List的具体类型Class，例如：ArrayList.class
     * @param <T>     元素泛型数据类型
     * @return 返回指定泛型的集合
     * @throws JsonProcessingException 解析异常
     */
    public static <T> List<T> parseJsonStringToList(String content, Class<T> type) throws JsonProcessingException {
        checkContent(content);
        CollectionLikeType listType = TypeFactory.defaultInstance().constructCollectionLikeType(ArrayList.class, type);
        return OBJECT_MAPPER.readValue(content, listType);

    }

    /**
     * 解析Set数据
     *
     * @param content json字符串
     * @param type    Set的具体类型Class，例如：HashSet.class
     * @param <T>     元素泛型数据类型
     * @return 返回指定泛型的集合
     * @throws JsonProcessingException 解析异常
     */
    public static <T> Set<T> parseJsonStringToSet(String content, Class<T> type) throws JsonProcessingException {
        checkContent(content);
        CollectionLikeType setType = TypeFactory.defaultInstance().constructCollectionLikeType(HashSet.class, type);
        return OBJECT_MAPPER.readValue(content, setType);
    }

    /**
     * 解析集合JSON字符串
     *
     * @param content        集合JSON字符串
     * @param collectionType 集合类型Class，比如ArrayList.class，LinkedArrayList.class
     * @param type           集合元素泛型类型Class
     * @param <T>            元素泛型类型
     * @return 集合对象
     */
    public static <T> Collection<T> parseJsonStringToCollection(String content, Class<?> collectionType, Class<T> type) throws JsonProcessingException {
        checkContent(content);
        CollectionLikeType listType = TypeFactory.defaultInstance().constructCollectionLikeType(collectionType, type);
        return OBJECT_MAPPER.readValue(content, listType);
    }

    /**
     * 解析JSON字符串
     *
     * @param content   JSON字符串
     * @param valueType 反序列化数据类型Class
     * @param <T>       反序列化类型
     * @return 反序列化对象
     */
    public static <T> T parseJsonString(String content, Class<T> valueType) throws JsonProcessingException {
        checkContent(content);
        return OBJECT_MAPPER.readValue(content, valueType);
    }

    /**
     * 对象转二进制数组
     *
     * @param data 数据对象
     * @return 二进制数组
     */
    public static byte[] writeDataToByteArray(Object data) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }


    /**
     * 解析数组字符串数据
     *
     * @param content 数组字符串，例如：[1,2,3,4]
     * @return 字符串数据
     * @throws JsonProcessingException 解析异常
     */
    public static String[] parseJsonStringToStringArray(String content) throws JsonProcessingException {
        checkContent(content);
        ArrayType stringArrayType = TypeFactory.defaultInstance().constructArrayType(String.class);
        return OBJECT_MAPPER.readValue(content, stringArrayType);
    }

    private static void checkContent(String content) {
        if (content == null || "".equals(content)) {
            throw new IllegalArgumentException("请传入正确的JSON数据");
        }
    }
}
