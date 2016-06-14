package py.webcache.handler;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.webcache.exception.HandleCacheException;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * Created by pengyu on 2016/5/25.
 */
public class MemcachedCacheHandler implements CacheHandler {
    private Logger log = LoggerFactory.getLogger(MemcachedCacheHandler.class);
    private KeyGenerator keyGenerator;
    private CacheObjectSerializer cacheObjectSerializer;
    private KeySetSerializer keySetSerializer;
    private MemcachedClient client;
    public static final String KEYS_OF_URI_PREFIX = "keys_of_uri ";

    @Override
    public void putCacheObject(CacheObject cacheObject) {
        // TODO untested 需要结合memcachedClient来测试
        if (cacheObject == null || cacheObject.getKey() == null) {
            throw new NullPointerException("both cacheObject and cacheObject.key should not be null");
        }
        try {
            // 序列化对象
            byte[] cacheObjectBytes = cacheObjectSerializer.serialize(cacheObject);
            client.delete(cacheObject.getKey());
            client.add(cacheObject.getKey(), cacheObject.getExpiredTime(), cacheObjectBytes);
            addKeysForUri(cacheObject.getKey());
        } catch (Exception e) {
            log.error("an error occurred when put a CacheObject to cache server", e);
            throw new HandleCacheException("an error occurred when putting a CacheObject to cache server", e);
        }
    }

    @Override
    public CacheObject getCacheObject(String key) {
        // TODO untested 需要结合memcachedClient来测试
        if (key == null) {
            throw new NullPointerException("key should not be null");
        }
        try {
            Object cacheObjectBytes = client.get(key);
            if (cacheObjectBytes == null) {
//                throw new NullPointerException("the cacheObjectBytes from the cache server is null");
                return null;
            }
            return cacheObjectSerializer.deserialize((byte[]) cacheObjectBytes);
        } catch (Exception e) {
            log.error("an error occurred when getting a CacheObject from cache server", e);
            throw new HandleCacheException("an error occurred when getting a CacheObject from cache server", e);
        }
    }

    @Override
    public void delete(String key) {
        // TODO untested 需要结合memcachedClient来测试
        if (key == null) {
            throw new NullPointerException("key should not be null");
        }
        try {
            client.delete(key);
            deleteKeyForUri(key);
        } catch (Exception e) {
            log.error("an error occurred when getting a CacheObject from cache server", e);
            throw new HandleCacheException("an error occurred when getting a CacheObject from cache server", e);
        }
    }

    @Override
    public Set<String> getKeysForUri(String uri) {
        // TODO untested 需要结合memcachedClient来测试
        if (uri == null) {
            throw new NullPointerException("uri should not be null");
        }
        try {

            Object keySetObject = client.get(uri);
            if (keySetObject == null) {
//                throw new NullPointerException("the cacheObjectBytes from the cache server is null");
                return null;
            }
            return keySetSerializer.deserialize((byte[]) keySetObject);
        } catch (Exception e) {
            log.error("an error occurred when getting a CacheObject from cache server", e);
            throw new HandleCacheException("an error occurred when getting a CacheObject from cache server", e);
        }
    }

    private void addKeysForUri(String key) throws InterruptedException, MemcachedException, TimeoutException {
        if (key == null) {
            throw new NullPointerException("key should not be null");
        }
        String uri = keyGenerator.getUriFromKey(key);
        Set<String> keySet = getKeysForUri(uri);
        if (keySet == null) {
            keySet = new HashSet<>();
        }
        if (!keySet.contains(key)) {
            keySet.add(key);
            byte[] bytes = keySetSerializer.serialize(keySet);
            client.delete(uri);
            client.add(uri, Integer.MAX_VALUE, bytes); // todo 此处放的超时时间过长，待优化
        }
    }
    private void deleteKeyForUri(String key) throws InterruptedException, MemcachedException, TimeoutException {
        if (key == null) {
            throw new NullPointerException("key should not be null");
        }
        String uri = keyGenerator.getUriFromKey(key);
        Set<String> keySet = getKeysForUri(uri);
        if (keySet == null) {
            keySet = new HashSet<>();
        }
        if (keySet.contains(key)) {
            keySet.remove(key);
            byte[] bytes = keySetSerializer.serialize(keySet);
            client.delete(uri);
            client.add(uri, Integer.MAX_VALUE, bytes); // todo 此处放的超时时间过长，待优化
        }
    }

    public CacheObjectSerializer getCacheObjectSerializer() {
        return cacheObjectSerializer;
    }

    public void setCacheObjectSerializer(CacheObjectSerializer cacheObjectSerializer) {
        this.cacheObjectSerializer = cacheObjectSerializer;
    }

    public KeySetSerializer getKeySetSerializer() {
        return keySetSerializer;
    }

    public void setKeySetSerializer(KeySetSerializer keySetSerializer) {
        this.keySetSerializer = keySetSerializer;
    }

    public MemcachedClient getClient() {
        return client;
    }

    public void setClient(MemcachedClient client) {
        this.client = client;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }
}
