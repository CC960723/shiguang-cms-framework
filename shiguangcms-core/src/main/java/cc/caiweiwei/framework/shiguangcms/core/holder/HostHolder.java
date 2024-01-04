package cc.caiweiwei.framework.shiguangcms.core.holder;

/**
 * 描述：host持有器
 *
 * @author CaiZhengwei
 * @since 2024/1/1 14:57
 */
public class HostHolder {

    private static final ThreadLocal<String> HOST_CONTEXT = new ThreadLocal<>();

    public static void setHost(String host) {
        HOST_CONTEXT.set(host);
    }

    public static String getHost() {
        return HOST_CONTEXT.get();
    }

    public static void removeHostContext() {
        HOST_CONTEXT.remove();
    }
}
