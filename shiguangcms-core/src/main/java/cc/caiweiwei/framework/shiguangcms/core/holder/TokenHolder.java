package cc.caiweiwei.framework.shiguangcms.core.holder;
/**
 * 描述：token持有器
 *
 * @author CaiZhengwei
 * @since 2023/12/31 17:13
 */
public class TokenHolder {

    private static final ThreadLocal<String> TOKEN_CONTEXT = new ThreadLocal<>();

    public static void setToken(String token) {
        TOKEN_CONTEXT.set(token);
    }

    public static String getToken() {
        return TOKEN_CONTEXT.get();
    }

    public static void removeTokenContext() {
        TOKEN_CONTEXT.remove();
    }
}
