package cc.caiweiwei.framework.shiguangcms.core.holder;
/**
 * 描述：traceId持有器
 *
 * @author CaiZhengwei
 * @since 2023/12/31 17:14
 */
public class TraceIdHolder {

    private static final ThreadLocal<Long> CONTEXT_ID_CONTEXT = new ThreadLocal<>();

    public static void setTraceId(Long traceId) {
        CONTEXT_ID_CONTEXT.set(traceId);
    }

    public static Long getTraceId() {
        return CONTEXT_ID_CONTEXT.get();
    }

    public static void removeTraceIdContext() {
        CONTEXT_ID_CONTEXT.remove();
    }
}
