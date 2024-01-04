package cc.caiweiwei.framework.shiguangcms.core.holder;

/**
 * 描述：userId持有器
 *
 * @author CaiZhengwei
 * @since 2023/12/31 17:14
 */
public class UserIdHolder {

    private static final ThreadLocal<Integer> userIdContext = new ThreadLocal<>();

    public static Integer getUserId() {
        return userIdContext.get();
    }

    public static void setUserId(Integer userId) {
        userIdContext.set(userId);
    }

    public static void removeUserIdContext() {
        userIdContext.remove();
    }
}
