package py.webcache.config.pojo;

import java.util.Set;

/**
 * Created by pengyu on 2016/5/12.
 */
public class Trigger {
    private Id id = new Id();
    private String cacheUri;
    private String triggerUri;
    private Strategy strategy = Strategy.clear;
    private Scope scope = Scope.all;
    private Set<Condition> conditions;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public enum Strategy {
        refresh, clear
    }

    public enum Scope {
        all, specific
    }


    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Set<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(Set<Condition> conditions) {
        this.conditions = conditions;
    }

    public String getCacheUri() {
        return cacheUri;
    }

    public void setTriggerUri(String triggerUri) {
        this.triggerUri = triggerUri;
        id.triggerUri = triggerUri;
    }

    public void setCacheUri(String cacheUri) {
        this.cacheUri = cacheUri;
        id.cacheUri = cacheUri;
    }

    public String getTriggerUri() {
        return triggerUri;
    }

    public static class Id {
        private String cacheUri;
        private String triggerUri;

        public Id() {
        }

        public Id(String cacheUri, String triggerUri) {
            this.cacheUri = cacheUri;
            this.triggerUri = triggerUri;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (obj instanceof Id) {
                Id objId = (Id) obj;
                return (this.cacheUri == null && objId.cacheUri == null || this.cacheUri.equals(objId.cacheUri))
                        && (this.triggerUri == null && objId.triggerUri == null || this.triggerUri.equals(objId.triggerUri));
            }
            return false;
        }

        @Override
        public int hashCode() {
            return cacheUri.hashCode() + triggerUri.hashCode();
        }
    }
}
