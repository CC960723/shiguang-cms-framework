package cc.caiweiwei.framework.shiguangcms.core.entity;
/**
 * 描述：IdName实体
 *
 * @author CaiZhengwei
 * @since 2023/12/31 17:10
 */
public class IdName {

    /**
     * id值
     */
    private Long id;

    /**
     * name值
     */
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "IdName{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
