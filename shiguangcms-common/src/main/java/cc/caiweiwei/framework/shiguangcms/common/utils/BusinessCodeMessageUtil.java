package cc.caiweiwei.framework.shiguangcms.common.utils;

import cc.caiweiwei.framework.shiguangcms.core.IBusinessCode;
import cc.caiweiwei.framework.shiguangcms.core.holder.TraceIdHolder;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * 描述：业务码消息工具类
 *
 * @author CaiZhengwei
 * @since 2023/12/31 17:59
 */
public class BusinessCodeMessageUtil {

    private static MessageSource messageSource;

    private static String serviceName;

    public static void init(MessageSource messageSource, String serviceName) {
        BusinessCodeMessageUtil.messageSource = messageSource;
        BusinessCodeMessageUtil.serviceName = serviceName;
    }

    /**
     * 获取业务码信息描述
     *
     * @param businessCode 业务码
     * @return 业务码描述信息
     */
    public static String getBusinessCodeMessage(IBusinessCode businessCode) {
        return getBusinessCodeMessage(businessCode, null, String.valueOf(TraceIdHolder.getTraceId()), serviceName);
    }


    /**
     * 获取业务码信息描述
     *
     * @param businessCode   业务码
     * @param defaultMessage 在没有找到配置的业务码情况之下，将返回默认的信息描述
     * @return 业务码描述信息
     */
    public static String getBusinessCodeMessage(IBusinessCode businessCode, String defaultMessage) {
        return getBusinessCodeMessage(businessCode, defaultMessage, String.valueOf(TraceIdHolder.getTraceId()), serviceName, defaultMessage);
    }

    /**
     * 获取业务码信息描述
     *
     * @param businessCode   业务码
     * @param defaultMessage 在没有找到配置的业务码情况之下，将返回默认的信息描述
     * @param args           扩展参数。下标从0开始
     * @return 业务码描述信息
     */
    private static String getBusinessCodeMessage(IBusinessCode businessCode, String defaultMessage, Object... args) {
        return getBusinessCodeMessage(businessCode, defaultMessage, Locale.SIMPLIFIED_CHINESE, args);
    }


    /**
     * 获取业务码信息描述
     *
     * @param businessCode   业务码
     * @param defaultMessage 在没有找到配置的业务码情况之下，将返回默认的信息描述
     * @param locale         地区
     * @param args           扩展参数。下标从0开始
     * @return 业务码描述信息
     */
    public static String getBusinessCodeMessage(IBusinessCode businessCode, String defaultMessage, Locale locale, Object... args) {
        if (businessCode == null) {
            return defaultMessage;
        }
        return messageSource.getMessage(businessCode.getBusinessCode(), args, defaultMessage, locale);
    }
}
