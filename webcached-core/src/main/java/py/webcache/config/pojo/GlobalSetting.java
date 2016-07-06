package py.webcache.config.pojo;

/**
 * Created by pengyu on 2016/5/17.
 */
public class GlobalSetting {
    private String forceUpdateName = "force_update";
    private String forceUpdateValue = "mycached";
    private String contextPath = null;
    private Integer port = null;

    public String getForceUpdateName() {
        return forceUpdateName;
    }

    public void setForceUpdateName(String forceUpdateName) {
        this.forceUpdateName = forceUpdateName;
    }

    public String getForceUpdateValue() {
        return forceUpdateValue;
    }

    public void setForceUpdateValue(String forceUpdateValue) {
        this.forceUpdateValue = forceUpdateValue;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
