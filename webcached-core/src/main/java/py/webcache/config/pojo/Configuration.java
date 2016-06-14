package py.webcache.config.pojo;

import java.util.Map;
import java.util.Set;

/**
 * Web端缓存配置对象
 * Created by pengyu on 2016/5/12.
 */
public class Configuration {
    private GlobalSetting globalSetting;
    private Map<String, CacheConfig> caches;
    private Set<Trigger> triggers;
    private Map<String, Set<Trigger>> triggersGroupByCacheUri;
    private Map<String, Set<Trigger>> triggersGroupByTriggerUri;
    private Map<Trigger.Id, Trigger> triggersGroupById;

    public Map<String, CacheConfig> getCaches() {
        return caches;
    }

    public void setCaches(Map<String, CacheConfig> caches) {
        this.caches = caches;
    }

    public Set<Trigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(Set<Trigger> triggers) {
        this.triggers = triggers;
    }

    public GlobalSetting getGlobalSetting() {
        return globalSetting;
    }

    public void setGlobalSetting(GlobalSetting globalSetting) {
        this.globalSetting = globalSetting;
    }

    public Map<String, Set<Trigger>> getTriggersGroupByCacheUri() {
        return triggersGroupByCacheUri;
    }

    public void setTriggersGroupByCacheUri(Map<String, Set<Trigger>> triggersGroupByCacheUri) {
        this.triggersGroupByCacheUri = triggersGroupByCacheUri;
    }

    public Map<String, Set<Trigger>> getTriggersGroupByTriggerUri() {
        return triggersGroupByTriggerUri;
    }

    public void setTriggersGroupByTriggerUri(Map<String, Set<Trigger>> triggersGroupByTriggerUri) {
        this.triggersGroupByTriggerUri = triggersGroupByTriggerUri;
    }

    public Map<Trigger.Id, Trigger> getTriggersGroupById() {
        return triggersGroupById;
    }

    public void setTriggersGroupById(Map<Trigger.Id, Trigger> triggersGroupById) {
        this.triggersGroupById = triggersGroupById;
    }
}
