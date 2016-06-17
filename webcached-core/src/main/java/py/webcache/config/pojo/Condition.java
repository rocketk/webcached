package py.webcache.config.pojo;

import py.webcache.util.BeanUtil;

/**
 * Created by pengyu on 2016/5/12.
 */
public class Condition {
    private Type type = Type.paramEqual;
    private String name;
    private String value;

    public enum Type{
        paramEqual, nonParam
    }

    public Condition() {
    }

    public Condition(Type type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof Condition) {
            Condition another = (Condition) obj;
            return BeanUtil.equalsWithEachField(this, another);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (type != null ? type.hashCode() : 0)
                + (name != null ? name.hashCode() : 0)
                + (value != null ? value.hashCode() : 0);
    }
}
