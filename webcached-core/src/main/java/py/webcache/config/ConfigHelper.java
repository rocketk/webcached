package py.webcache.config;

import py.webcache.config.pojo.*;
import py.webcache.exception.NoSuchCacheConfigException;
import py.webcache.util.CollectionUtil;

import java.util.*;

/**
 * Created by pengyu on 2016/5/12.
 */
public class ConfigHelper {
    protected Configuration configuration;

    /**
     * 将paramMap转换成排序的TreeMap
     * 会根据配置对象中的 parameter.type 和 parameter.excludeEmpty 来控制treemap中包含或忽略哪些参数
     *
     * @param uri
     * @param paramMap
     * @return
     */
    public TreeMap<String, String[]> getParamTreeMap(String uri, Map<String, String[]> paramMap) {
        // 排序
        TreeMap<String, String[]> treeMap = new TreeMap<String, String[]>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        CacheConfig cacheConfig = getCacheConfigByUri(uri);
        if (cacheConfig == null) {
            throw new NoSuchCacheConfigException("no such cache-config found in the configuration file.");
        }
        // 复制参数，根据type和exceptions做筛选
        Set exceptions = cacheConfig.getParameter().getExceptions();
        switch (cacheConfig.getParameter().getType()) {
            case excludeAll:
                copyIncludedItems(paramMap, treeMap, exceptions);
                break;
            case includeAll:
                copyAllItemsExclude(paramMap, treeMap, exceptions);
                break;
        }
        return treeMap;
    }

    /**
     * 获取uri对应的缓存配置，当找不到时，返回null
     *
     * @param uri
     * @return
     */
    private CacheConfig getCacheConfigByUri(String uri) {
        if (uri == null || uri.length() == 0) {
            throw new IllegalArgumentException("argument uri is empty");
        }
        CacheConfig cacheConfig = configuration.getCaches().get(uri);
        return cacheConfig;
    }

    private void copyIncludedItems(Map<String, String[]> origin, Map<String, String[]> target, Set<String> includes) {
        if (includes == null || includes.size() == 0) {
            return;
        }
        for (String key : includes) {
            String[] value = origin.get(key);
            if (!CollectionUtil.isStrArrEmpty(value)) {
                target.put(key, value);
            }
        }
    }

    private void copyAllItemsExclude(Map<String, String[]> origin, Map<String, String[]> target, Set<String> excludes) {
        if (excludes == null || excludes.size() == 0) {
            throw new IllegalArgumentException("includes should not be null");
        }
        for (Map.Entry<String, String[]> entry : origin.entrySet()) {
            if (!CollectionUtil.isStrArrEmpty(entry.getValue()) && !excludes.contains(entry.getKey())) {
                target.put(entry.getKey(), entry.getValue());
            }
        }
    }


//    private void clearNullValueEntry(Map<String, String[]> paramMap) {
//        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
//            String[] value = entry.getValue();
//            // 如果value为空，则删除这个key
//            if (value == null || value.length == 0) {
//                paramMap.remove(entry.getKey());
//            } else {
//                // 当value数组的每一个元素都是空时，删除这个key
//                boolean emptyValue = true;
//                for (String s : value) {
//                    if (s != null && s.length() > 0) {
//                        emptyValue = false;
//                        break;
//                    }
//                }
//                if (emptyValue) {
//                    paramMap.remove(entry.getKey());
//                }
//            }
//        }
//    }

    /**
     * 判断uri是否支持缓存
     *
     * @param uri
     * @return
     */
    public boolean isSupportCache(String uri) {
        CacheConfig cacheConfig = getCacheConfigByUri(uri);
        return cacheConfig != null;
    }

//    public boolean isTrigger(String uri) {
//
//    }

    /**
     * 根据uri找出它所对应的缓存配置的有效时间长度，单位秒
     * 当uri不支持缓存时，抛出异常
     *
     * @param uri
     * @return
     */
    public int getExpiredTime(String uri) {
        CacheConfig cacheConfig = getCacheConfigByUri(uri);
        if (cacheConfig == null) {
            throw new NoSuchCacheConfigException("no such cache-config found in the configuration file.");
        }
        return cacheConfig.getExpiredTime();
    }

    /**
     * 判断uri对应的缓存配置是否支持指定状态码status，status通常是由response.getStatus()获得
     *
     * @param uri
     * @param status
     * @return
     */
    public boolean isSupportStatus(String uri, int status) {
        CacheConfig cacheConfig = getCacheConfigByUri(uri);
        if (cacheConfig == null) {
            throw new NoSuchCacheConfigException("no such cache-config found in the configuration file.");
        }
        return cacheConfig.getSupportStatus() != null && cacheConfig.getSupportStatus().contains(status);
    }

    /**
     * 获取全局变量
     *
     * @return
     */
    public GlobalSetting getGlobalSetting() {
        return configuration.getGlobalSetting();
    }

    public boolean isTrigger(String uri) {
        if (uri == null || uri.length() == 0) {
            throw new IllegalArgumentException("argument uri is empty");
        }
        Map<String, Set<Trigger>> triggersGroupByTriggerUri = configuration.getTriggersGroupByTriggerUri();
        Set<Trigger> triggerSet = triggersGroupByTriggerUri.get(uri);
        return triggerSet != null && triggerSet.size() > 0;
    }

    public Set<String> cachedUriSetForTrigger(String uri) {
        if (uri == null || uri.length() == 0) {
            throw new IllegalArgumentException("argument uri is empty");
        }
        Set<String> stringSet = new HashSet<>();
        Map<String, Set<Trigger>> triggersGroupByTriggerUri = configuration.getTriggersGroupByTriggerUri();
        Set<Trigger> triggerSet = triggersGroupByTriggerUri.get(uri);
        if (triggerSet != null) {
            for (Trigger trigger : triggerSet) {
                stringSet.add(trigger.getCacheUri());
            }
        }
        return stringSet;
    }

    public Trigger getTrigger(String triggerUri, String cachedUri) {
        if (triggerUri == null || triggerUri.length() == 0 || cachedUri == null || cachedUri.length() == 0) {
            throw new IllegalArgumentException("argument triggerUri/cachedUri is empty");
        }
        Map<Trigger.Id, Trigger> triggersGroupById = configuration.getTriggersGroupById();
        Trigger trigger = triggersGroupById.get(new Trigger.Id(cachedUri, triggerUri));
        return trigger;
    }

    public Set<Condition> getConditions(String triggerUri, String cachedUri) {
        if (triggerUri == null || triggerUri.length() == 0 || cachedUri == null || cachedUri.length() == 0) {
            throw new IllegalArgumentException("argument triggerUri/cachedUri is empty");
        }
        Map<Trigger.Id, Trigger> triggersGroupById = configuration.getTriggersGroupById();
        Trigger trigger = triggersGroupById.get(new Trigger.Id(cachedUri, triggerUri));
        if (trigger == null) {
//            throw new NullPointerException(String.format("the trigger is null for triggerUri = %s and cachedUri = %s", triggerUri, cachedUri));
            return null;
        }
        return trigger.getConditions();
    }


    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
