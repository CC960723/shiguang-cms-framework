package cc.caiweiwei.framework.shiguangcms.common.utils;

import cc.caiweiwei.framework.shiguangcms.common.constant.CommonConstant;
import cc.caiweiwei.framework.shiguangcms.common.exception.HttpSendFailException;
import cc.caiweiwei.framework.shiguangcms.core.holder.HostHolder;
import cc.caiweiwei.framework.shiguangcms.core.holder.TraceIdHolder;
import cc.caiweiwei.framework.shiguangcms.core.holder.UserIdHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述：Http客户端工具类
 *
 * @author CaiZhengwei
 * @since 2023/12/31 18:21
 */
public class HttpClientUtil {

    private static final CloseableHttpClient closeableHttpClient;

    private static final String APPLICATION_JSON = "application/json";

    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    static {
        closeableHttpClient = HttpClients.createDefault();
        Runtime.getRuntime().addShutdownHook(new Thread(HttpClientUtil::destroyHttpClient, "关闭HTTPClient钩子线程"));
    }

    /**
     * 发送GET请求
     *
     * @param requestUri   请求路径
     * @param responseType 响应结果类型Class
     * @param <T>          响应结果类型
     * @return 响应结果
     */
    public static <T> T doGet(String requestUri, Class<T> responseType) {
        return doGet(requestUri, null, responseType);
    }

    /**
     * 发送GET请求
     *
     * @param requestUri   请求路径
     * @param queryParam   请求查询参数
     * @param responseType 响应结果类型Class
     * @param <T>          响应结果类型
     * @return 响应结果
     */
    public static <T> T doGet(String requestUri, Map<String, Object> queryParam, Class<T> responseType) {
        if (requestUri == null || responseType == null) {
            throw new InvalidParameterException("无效参数，请指定请求路径和响应结果类型！");
        }
        try {
            if (queryParam != null && !queryParam.isEmpty()) {
                if (!requestUri.contains("?")) {
                    requestUri = requestUri + "?";
                } else {
                    requestUri = requestUri + "&";
                }
                requestUri = requestUri + parseQueryParam(queryParam);
            }
            HttpGet httpGet = new HttpGet(obtainUri(requestUri));
            transportBusinessHeader(httpGet);
            return closeableHttpClient.execute(httpGet, response -> {
                String responseContent = obtainResponseBodyAndCloseResponseStream(response, true);
                return JsonUtil.parseJsonString(responseContent, responseType);
            });
        } catch (IOException e) {
            throw new HttpSendFailException(e);
        }

    }

    /**
     * 发送POST请求
     *
     * @param requestUri   请求路径
     * @param requestBody  请求体，使用Content-Type:application/json
     * @param responseType 响应结果类型Class
     * @param <T>          响应结果类型
     * @return 响应结果
     */
    public static <T> T doApplicationJsonPost(String requestUri, Object requestBody, Class<T> responseType) {
        return doPost(requestUri, requestBody, APPLICATION_JSON, responseType);
    }

    /**
     * 发送POST请求
     *
     * @param requestUri   请求路径
     * @param requestBody  请求体，使用Content-Type:multipart/form-data
     * @param responseType 响应结果类型Class
     * @param <T>          响应结果类型
     * @return 响应结果
     */
    public static <T> T doMultipartFormDataPost(String requestUri, Map<String, String> requestBody, Class<T> responseType) {
        return doPost(requestUri, requestBody, MULTIPART_FORM_DATA, responseType);
    }

    /**
     * 发送POST请求
     *
     * @param requestUri         请求路径
     * @param requestBody        请求体，默认使用Content-Type:application/json
     * @param requestContentType 请求内容类型
     * @param responseType       响应结果类型Class
     * @param <T>                响应结果类型
     * @return 响应结果
     */
    public static <T> T doPost(String requestUri, Object requestBody, String requestContentType, Class<T> responseType) {
        if (requestUri == null || responseType == null) {
            throw new InvalidParameterException("无效参数，请指定请求路径和响应结果类型！");
        }
        try (HttpEntity requestEntity = obtainHttpEntity(requestBody, requestContentType)) {
            HttpPost httpPost = new HttpPost(obtainUri(requestUri));
            httpPost.setEntity(requestEntity);
            transportBusinessHeader(httpPost);
            return closeableHttpClient.execute(httpPost, response -> {
                String responseContent = obtainResponseBodyAndCloseResponseStream(response, true);
                return JsonUtil.parseJsonString(responseContent, responseType);
            });
        } catch (IOException e) {
            throw new HttpSendFailException(e);
        }
    }

    /**
     * <p>发送POST请求，返回参数是有序列表</p>
     * <p>ps: 在类字面量上不允许使用参数化类型，而是使用原始类型<a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html">jsl 15.8.2. Class Literals</a>，在声明泛型的角度又可以使用参数化类型<a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.5">jsl 4.5. Parameterized Types</a>。
     * 于是，在定义泛型边界的时候不指定参数化类型会提示不建议使用原始类型的警告。在当前的方法声明中Class<? extends List>是为了获取原始类型的类字面量。
     * 所以，添加@SuppressWarnings抑制使用泛型声明时校验使用原始类型的警告</p>
     *
     * @param requestUri  请求路径
     * @param requestBody 请求体，使用Content-Type:application/json
     * @param listType    响应有序列表类型Class
     * @param itemType    响应列表实体类型Class
     * @param <T>         响应列表实体类型
     * @return 响应列表结果
     */
    @SuppressWarnings(value = {"rawtypes"})
    public static <T> List<T> doApplicationJsonPostAsList(String requestUri, Object requestBody, Class<? extends List> listType, Class<T> itemType) {
        return (List<T>) doPostAsCollection(requestUri, requestBody, APPLICATION_JSON, listType, itemType);
    }

    /**
     * 发送POST请求，返回参数是无序不重复列表<br>
     * <p>ps: 在类字面量上不允许使用参数化类型，而是使用原始类型<a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html">jsl 15.8.2. Class Literals</a>，在声明泛型的角度又可以使用参数化类型<a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.5">jsl 4.5. Parameterized Types</a>。
     * 于是，在定义泛型边界的时候不指定参数化类型会提示不建议使用原始类型的警告。在当前的方法声明中Class<? extends Set>是为了获取原始类型的类字面量。
     * 所以，添加@SuppressWarnings抑制使用泛型声明时校验使用原始类型的警告</p>
     *
     * @param requestUri  请求路径
     * @param requestBody 请求体，使用Content-Type:application/json
     * @param setType     响应无序不重复列表类型Class
     * @param itemType    响应列表实体类型Class
     * @param <T>         响应列表实体类型
     * @return 响应列表结果
     */
    @SuppressWarnings(value = {"rawtypes"})
    public static <T> Set<T> doApplicationJsonPostAsSet(String requestUri, Object requestBody, Class<? extends Set> setType, Class<T> itemType) {
        return (Set<T>) doPostAsCollection(requestUri, requestBody, APPLICATION_JSON, setType, itemType);
    }


    /**
     * <p>发送POST请求，返回参数是有序列表</p>
     * <p>ps: 在类字面量上不允许使用参数化类型，而是使用原始类型<a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html">jsl 15.8.2. Class Literals</a>，在声明泛型的角度又可以使用参数化类型<a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.5">jsl 4.5. Parameterized Types</a>。
     * 于是，在定义泛型边界的时候不指定参数化类型会提示不建议使用原始类型的警告。在当前的方法声明中Class<? extends List>是为了获取原始类型的类字面量。
     * 所以，添加@SuppressWarnings抑制使用泛型声明时校验使用原始类型的警告</p>
     *
     * @param requestUri  请求路径
     * @param requestBody 请求体，使用Content-Type:multipart/form-data
     * @param listType    响应有序列表类型Class
     * @param itemType    响应列表实体类型Class
     * @param <T>         响应列表实体类型
     * @return 响应列表结果
     */
    @SuppressWarnings(value = {"rawtypes"})
    public static <T> List<T> doMultipartFormDataPostAsList(String requestUri, Map<String, String> requestBody, Class<? extends List> listType, Class<T> itemType) {
        return (List<T>) doPostAsCollection(requestUri, requestBody, MULTIPART_FORM_DATA, listType, itemType);
    }

    /**
     * 发送POST请求，返回参数是无序不重复列表<br>
     * <p>ps: 在类字面量上不允许使用参数化类型，而是使用原始类型<a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html">jsl 15.8.2. Class Literals</a>，在声明泛型的角度又可以使用参数化类型<a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.5">jsl 4.5. Parameterized Types</a>。
     * 于是，在定义泛型边界的时候不指定参数化类型会提示不建议使用原始类型的警告。在当前的方法声明中Class<? extends Set>是为了获取原始类型的类字面量。
     * 所以，添加@SuppressWarnings抑制使用泛型声明时校验使用原始类型的警告</p>
     *
     * @param requestUri  请求路径
     * @param requestBody 请求体，使用Content-Type:multipart/form-data
     * @param setType     响应无序不重复列表类型Class
     * @param itemType    响应列表实体类型Class
     * @param <T>         响应列表实体类型
     * @return 响应列表结果
     */
    @SuppressWarnings(value = {"rawtypes"})
    public static <T> Set<T> doMultipartFormDataPostAsSet(String requestUri, Map<String, String> requestBody, Class<? extends Set> setType, Class<T> itemType) {
        return (Set<T>) doPostAsCollection(requestUri, requestBody, MULTIPART_FORM_DATA, setType, itemType);
    }


    /**
     * 发送POST请求，返回参数是列表
     * <p>ps: 在类字面量上不允许使用参数化类型，而是使用原始类型<a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html">jsl 15.8.2. Class Literals</a>，在声明泛型的角度又可以使用参数化类型<a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.5">jsl 4.5. Parameterized Types</a>。
     * 于是，在定义泛型边界的时候不指定参数化类型会提示不建议使用原始类型的警告。在当前的方法声明中Class<? extends Collection>是为了获取原始类型的类字面量。
     * 所以，添加@SuppressWarnings抑制使用泛型声明时校验使用原始类型的警告</p>
     *
     * @param requestUri         请求路径
     * @param requestBody        请求体，默认使用Content-Type:application/json
     * @param requestContentType 请求内容类型
     * @param collectionType     响应列表类型Class
     * @param itemType           响应列表实体类型Class
     * @param <T>                响应列表实体类型
     * @return 响应列表结果
     */
    @SuppressWarnings(value = {"rawtypes"})
    public static <T> Collection<T> doPostAsCollection(String requestUri, Object requestBody, String requestContentType, Class<? extends Collection> collectionType, Class<T> itemType) {
        if (requestUri == null) {
            throw new InvalidParameterException("无效参数，请指定请求路径！");
        }
        if (requestContentType == null || "".equals(requestContentType)) {
            throw new InvalidParameterException("无效参数，请指定请求内容类型！");
        }
        try (HttpEntity requestEntity = obtainHttpEntity(requestBody, requestContentType)) {
            HttpPost httpPost = new HttpPost(obtainUri(requestUri));
            httpPost.setEntity(requestEntity);
            transportBusinessHeader(httpPost);
            return closeableHttpClient.execute(httpPost, response -> {
                String responseContent = obtainResponseBodyAndCloseResponseStream(response, false);
                return JsonUtil.parseJsonStringToCollection(responseContent, collectionType, itemType);
            });
        } catch (IOException e) {
            throw new HttpSendFailException(e);
        }
    }

    /**
     * 发送POST请求 JSON请求类型 返回Map
     *
     * @param requestUri  请求路径
     * @param requestBody 请求体，默认使用Content-Type:application/json
     * @param keyType     Map key类型
     * @param valueType   Map value类型
     * @param <K>         key 泛型
     * @param <V>         value 泛型
     * @return 响应值Map
     */
    public static <K, V> Map<K, V> doApplicationJsonPostAsMap(String requestUri, Object requestBody, Class<K> keyType, Class<V> valueType) {
        return doPostAsMap(requestUri, requestBody, APPLICATION_JSON, keyType, valueType);
    }

    /**
     * 发送POST请求 表单请求类型，返回参数是Map
     *
     * @param requestUri  请求路径
     * @param requestBody 请求体，使用Content-Type:multipart/form-data
     * @param keyType     Map key类型
     * @param valueType   Map value类型
     * @param <K>         key 泛型
     * @param <V>         value 泛型
     * @return 响应值Map
     */
    public static <K, V> Map<K, V> doMultipartFormDataPostAsMap(String requestUri, Map<String, String> requestBody, Class<K> keyType, Class<V> valueType) {
        return doPostAsMap(requestUri, requestBody, MULTIPART_FORM_DATA, keyType, valueType);
    }

    /**
     * 发送POST请求，返回参数是Map
     *
     * @param requestUri         请求路径
     * @param requestBody        请求体，默认使用Content-Type:application/json
     * @param requestContentType 请求内容类型
     * @param keyType            Map key类型
     * @param valueType          Map value类型
     * @param <K>                key 泛型
     * @param <V>                value 泛型
     * @return 响应值Map
     */
    public static <K, V> Map<K, V> doPostAsMap(String requestUri, Object requestBody, String requestContentType, Class<K> keyType, Class<V> valueType) {
        if (requestUri == null) {
            throw new InvalidParameterException("无效参数，请指定请求路径！");
        }
        if (requestContentType == null || "".equals(requestContentType)) {
            throw new InvalidParameterException("无效参数，请指定请求内容类型！");
        }
        try (HttpEntity requestEntity = obtainHttpEntity(requestBody, requestContentType)) {
            HttpPost httpPost = new HttpPost(obtainUri(requestUri));
            httpPost.setEntity(requestEntity);
            transportBusinessHeader(httpPost);
            return closeableHttpClient.execute(httpPost, response -> {
                String responseContent = obtainResponseBodyAndCloseResponseStream(response, false);
                return JsonUtil.parseJsonStringToMap(responseContent, keyType, valueType);
            });
        } catch (IOException e) {
            throw new HttpSendFailException(e);
        }
    }

    /**
     * 发送PUT请求
     *
     * @param requestUri  请求路径
     * @param requestBody 请求体
     */
    public static void doPut(String requestUri, Object requestBody) {
        if (requestUri == null) {
            throw new InvalidParameterException("无效参数，请指定请求路径和响应结果类型！");
        }

        try (HttpEntity requestEntity = obtainHttpEntity(requestBody, APPLICATION_JSON)) {
            HttpPut httpPut = new HttpPut(obtainUri(requestUri));
            httpPut.setEntity(requestEntity);
            transportBusinessHeader(httpPut);
            executeNoneResponseBody(httpPut);
        } catch (IOException e) {
            throw new HttpSendFailException(e);
        }
    }

    /**
     * 发送DELETE请求
     *
     * @param requestUri  请求路径
     * @param requestBody 请求体
     */
    public static void doDelete(String requestUri, Object requestBody) {
        if (requestUri == null) {
            throw new InvalidParameterException("无效参数，请指定请求路径和响应结果类型！");
        }
        try (HttpEntity requestEntity = obtainHttpEntity(requestBody, "")) {
            HttpDelete httpDelete = new HttpDelete(obtainUri(requestUri));
            httpDelete.setEntity(requestEntity);
            transportBusinessHeader(httpDelete);
            executeNoneResponseBody(httpDelete);
        } catch (IOException e) {
            throw new HttpSendFailException(e);
        }
    }

    /**
     * 发送DELETE请求
     *
     * @param requestUri   请求路径
     * @param requestBody  请求体
     * @param responseType 响应结果类型Class
     * @param <T>          响应结果类型
     * @return 响应结果
     */
    public static <T> T doDelete(String requestUri, Object requestBody, Class<T> responseType) {
        if (requestUri == null || responseType == null) {
            throw new InvalidParameterException("无效参数，请指定请求路径和响应结果类型！");
        }
        try (HttpEntity requestEntity = obtainHttpEntity(requestBody, APPLICATION_JSON)) {
            HttpDelete httpDelete = new HttpDelete(obtainUri(requestUri));
            httpDelete.setEntity(requestEntity);
            transportBusinessHeader(httpDelete);
            return closeableHttpClient.execute(httpDelete, response -> {
                String responseContent = obtainResponseBodyAndCloseResponseStream(response, true);
                return JsonUtil.parseJsonString(responseContent, responseType);
            });
        } catch (IOException e) {
            throw new HttpSendFailException(e);
        }
    }


    public static void destroyHttpClient() {
        try {
            log.debug("正在关闭HTTPClient……");
            closeableHttpClient.close();
            log.debug("HTTPClient已关闭！");
        } catch (IOException e) {
            log.error("请求连接关闭失败！", e);
        }
    }

    private static String obtainResponseBodyAndCloseResponseStream(ClassicHttpResponse response, boolean checkBusinessCodeError) throws IOException, ParseException {
        checkHttpStatus(response.getCode());
        HttpEntity responseEntity = response.getEntity();
        String responseContent = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
        //检查是否有业务码错误
        if (checkBusinessCodeError) {
            checkResponseBusinessCodeError(responseContent);
        }
        EntityUtils.consumeQuietly(responseEntity);
        return responseContent;
    }

    private static void executeNoneResponseBody(ClassicHttpRequest httpRequest) throws IOException {
        int httpStatus = closeableHttpClient.execute(httpRequest, response -> {
            HttpEntity responseEntity = response.getEntity();
            String responseContent = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            checkResponseBusinessCodeError(responseContent);
            EntityUtils.consumeQuietly(responseEntity);
            return response.getCode();
        });
        checkHttpStatus(httpStatus);
    }

    private static void checkResponseBusinessCodeError(String responseContent) throws JsonProcessingException {
        if (responseContent == null || "".equals(responseContent)) {
            return;
        }
        Map<String, Object> responseMap = JsonUtil.parseJsonStringToMap(responseContent, String.class, Object.class);
        Object code = responseMap.get(CommonConstant.RESPONSE_CODE);
        if (code == null) {
            return;
        }
        if (!checkBusinessCodeValid(code.toString())) {
            throw new HttpSendFailException("服务方业务异常，异常码是：" + responseMap.get(CommonConstant.RESPONSE_CODE));
        }
    }

    private static void checkHttpStatus(int httpStatus) {
        if (httpStatus >= 200 && httpStatus < 300) {
            return;
        }
        switch (httpStatus) {
            case HttpStatus.SC_BAD_REQUEST:
                throw new HttpSendFailException("Bad Request!");
            case HttpStatus.SC_UNAUTHORIZED:
                throw new HttpSendFailException("Unauthorized!");
            case HttpStatus.SC_PAYMENT_REQUIRED:
                throw new HttpSendFailException("Payment Required!");
            case HttpStatus.SC_FORBIDDEN:
                throw new HttpSendFailException("Forbidden!");
            case HttpStatus.SC_NOT_FOUND:
                throw new HttpSendFailException("Not Found!");
            case HttpStatus.SC_METHOD_NOT_ALLOWED:
                throw new HttpSendFailException("Method Not Allowed!");
            case HttpStatus.SC_NOT_ACCEPTABLE:
                throw new HttpSendFailException("Not Acceptable!");
            case HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED:
                throw new HttpSendFailException("Proxy Authentication Required!");
            case HttpStatus.SC_SERVER_ERROR:
                throw new HttpSendFailException("Server Error!");
            case HttpStatus.SC_SERVICE_UNAVAILABLE:
                throw new HttpSendFailException("Service Unavailable!");
            case HttpStatus.SC_GATEWAY_TIMEOUT:
                throw new HttpSendFailException("Gateway Timeout!");
            default:
                throw new HttpSendFailException("请求失败！");
        }
    }

    private static String obtainUri(String requestUri) {
        String host = HostHolder.getHost();
        if (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }
        if (!requestUri.startsWith("/")) {
            requestUri = "/" + requestUri;
        }
        return host + requestUri;
    }


    @SuppressWarnings({"unchecked"})
    private static HttpEntity obtainHttpEntity(Object requestBody, String mimeType) {
        if (requestBody == null) {
            return null;
        }
        switch (mimeType) {
            case "application/json":
                return new ByteArrayEntity(JsonUtil.writeDataToByteArray(requestBody), ContentType.APPLICATION_JSON, null);
            case "multipart/form-data":
                Map<String, String> requestBodyMap = (Map<String, String>) requestBody;
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                for (Map.Entry<String, String> entry : requestBodyMap.entrySet()) {
                    builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.create("multipart/form-data", "UTF-8"));
                }
                return builder.build();
        }
        return new ByteArrayEntity(JsonUtil.writeDataToByteArray(requestBody), ContentType.APPLICATION_JSON, null);
    }


    private static String parseQueryParam(Map<String, Object> queryParam) throws JsonProcessingException {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : queryParam.entrySet()) {
            builder.append(entry.getKey()).append("=");
            if (entry.getValue() == null) {
                builder.append("&");
                continue;
            } else if (entry.getValue() instanceof String || entry.getValue() instanceof Number || entry.getValue().getClass().isPrimitive()) {
                builder.append(entry.getValue());
            } else if (entry.getValue() instanceof Collection) {
                Collection<?> array = (Collection<?>) entry.getValue();
                int index = 0;
                int length = array.size();
                for (Object item : array) {
                    builder.append(item);
                    if (index < length - 1) {
                        builder.append(",");
                    }
                    index++;
                }
                builder.delete(builder.length() - 1, builder.length());
            } else if (entry.getValue() instanceof Object[]) {
                Object[] array = (Object[]) entry.getValue();
                int length = array.length;
                for (int i = 0; i < length; i++) {
                    builder.append(array[i]);
                    if (i < length - 1) {
                        builder.append(",");
                    }
                }
            } else if (entry.getValue().getClass().isArray()) {
                String primitiveArrayJsonStr = JsonUtil.toJsonStr(entry.getValue());
                String[] array = JsonUtil.parseJsonStringToStringArray(primitiveArrayJsonStr);
                int length = array.length;
                for (int i = 0; i < length; i++) {
                    builder.append(array[i]);
                    if (i < length - 1) {
                        builder.append(",");
                    }
                }
                builder.delete(builder.length() - 1, builder.length());
            } else {
                throw new InvalidParameterException("无效的请求参数，数据类型不支持！");
            }
            builder.append("&");
        }
        if (builder.charAt(builder.length() - 1) == '&') {
            builder.delete(builder.length() - 1, builder.length());
        }
        return builder.toString();
    }

    private static void transportBusinessHeader(HttpUriRequestBase uriRequestBase) {
        Long traceId = TraceIdHolder.getTraceId();
        Integer userId = UserIdHolder.getUserId();
        if (traceId != null) {
            uriRequestBase.setHeader(CommonConstant.TRACE_ID_HEADER, traceId);
        }
        if (userId != null) {
            uriRequestBase.setHeader(CommonConstant.USER_ID_HEADER, userId);
        }
    }

    private static boolean checkBusinessCodeValid(String businessCode) {
        if (businessCode == null || "".equals(businessCode)) {
            return false;
        }
        return CommonConstant.SUCCESS_CODE.equals(businessCode);
    }
}
