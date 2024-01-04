package cc.caiweiwei.framework.shiguangcms.core.holder;

/**
 * 描述：totalSize持有器
 *
 * @author CaiZhengwei
 * @since 2023/12/31 17:14
 */
public class TotalSizeHolder {
    private static final ThreadLocal<Integer> totalSizeContext = new ThreadLocal<>();

    public static Integer getTotalSize() {
        return totalSizeContext.get();
    }

    public static void setTotalSize(Integer totalSize) {
        totalSizeContext.set(totalSize);
    }

    public static void removeTotalSizeContext() {
        totalSizeContext.remove();
    }
}
