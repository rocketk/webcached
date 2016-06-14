package py.webcache.config.pojo;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by pengyu on 2016/5/12.
 */
public class CacheConfig {
    private String uri;
    private Parameter parameter = new Parameter();
    private int expiredTime = 60;
    /**缓存到期后是否自动刷新缓存*/
    // TODO: 暂时先不提供自动刷新缓存的实现
    private boolean autoRefresh = false;
    private Set<Integer> supportStatus = new HashSet<>();

    {
        supportStatus.add(200);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public int getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(int expiredTime) {
        this.expiredTime = expiredTime;
    }

    public boolean isAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public Set<Integer> getSupportStatus() {
        return supportStatus;
    }

    public void setSupportStatus(Set<Integer> supportStatus) {
        this.supportStatus = supportStatus;
    }
}
