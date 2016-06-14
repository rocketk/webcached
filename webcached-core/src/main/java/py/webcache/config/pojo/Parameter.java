package py.webcache.config.pojo;

import java.util.Set;

/**
 * Created by pengyu on 2016/5/12.
 */
public class Parameter {
    private Type type = Type.includeAll;
    /**例外，当type为includeAll时，本项指的是exclude，而当type为excludeAll时，本项指的是include*/
    private Set<String> exceptions;

    public enum Type {
        includeAll, excludeAll
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Set<String> getExceptions() {
        return exceptions;
    }

    public void setExceptions(Set<String> exceptions) {
        this.exceptions = exceptions;
    }
}
