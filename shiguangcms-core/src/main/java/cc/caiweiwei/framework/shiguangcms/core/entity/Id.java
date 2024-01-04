package cc.caiweiwei.framework.shiguangcms.core.entity;
/**
 * 描述：Id实体
 *
 * @author CaiZhengwei
 * @since 2023/12/31 17:11
 */
public class Id {

    /**
     * id值
     */
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Id{" +
                "id=" + id +
                '}';
    }
}
