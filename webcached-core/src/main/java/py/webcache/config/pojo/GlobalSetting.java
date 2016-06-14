package py.webcache.config.pojo;

/**
 * Created by pengyu on 2016/5/17.
 */
public class GlobalSetting {
    private String forceUpdateName = "force_update";
    private String forceUpdateValue = "mycached";

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
}
