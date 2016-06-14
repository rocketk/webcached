package py.webcache.config.pojo;

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
}
