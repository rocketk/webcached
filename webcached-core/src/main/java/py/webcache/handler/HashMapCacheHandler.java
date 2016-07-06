package py.webcache.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 用HashMap实现缓存处理器，适用于单台服务器
 * Created by pengyu on 2016/7/4.
 */
public class HashMapCacheHandler implements CacheHandler {
    private KeyGenerator keyGenerator;
    private Map<String, CacheObject> cacheObjectMap = new HashMap<>();
    private Map<String, Set<String>> keySetMap = new HashMap<>();

    @Override
    public void putCacheObject(CacheObject cacheObject) {
        if (cacheObject == null || cacheObject.getKey() == null) {
            throw new NullPointerException("both cacheObject and cacheObject.key should not be null");
        }
        cacheObjectMap.put(cacheObject.getKey(), cacheObject);
        String uri = keyGenerator.getUriFromKey(cacheObject.getKey());
        if (keySetMap.get(uri) != null) {
            Set<String> keySet = keySetMap.get(uri);
            keySet.add(cacheObject.getKey());
        } else {
            Set<String> keySet = new HashSet<>();
            keySetMap.put(uri, keySet);
            keySet.add(cacheObject.getKey());
        }
    }

    @Override
    public CacheObject getCacheObject(String key) {
        removeIfTimeout(key);
        return cacheObjectMap.get(key);
    }

    @Override
    public void delete(String key) {
        cacheObjectMap.remove(key);
    }

    @Override
    public Set<String> getKeysForUri(String uri) {
        return keySetMap.get(uri);
    }

    /**
     * 当缓存已过有效期时，删除
     * @param key
     */
    private void removeIfTimeout(String key) {
        CacheObject cacheObject = cacheObjectMap.get(key);
        if (cacheObject != null) {
            long cost = (System.currentTimeMillis() - cacheObject.getPutTime()) / 1000; // 已消耗了多长时间，秒
            long left = cacheObject.getExpiredTime() - cost; // 还剩多少时间，秒
            if (left <= 0) {
                cacheObjectMap.remove(key);
            }
        }
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }
}
