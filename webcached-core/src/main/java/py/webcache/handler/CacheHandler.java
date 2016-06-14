package py.webcache.handler;

import java.util.Set;

/**
 * 缓存对象处理接口
 * Created by pengyu on 2016/5/12.
 */
public interface CacheHandler {

    /**
     * 将缓存对象存入缓存服务器
     * @param cacheObject
     */
    void putCacheObject(CacheObject cacheObject);

    /**
     * 从缓存服务器上获取指定key的缓存值
     * 当缓存不存在时，返回null
     * @param key
     * @return 需要计算出cost和left值
     */
    CacheObject getCacheObject(String key);

    /**
     * 从缓存服务器上清空指定key的缓存值
     * @param key
     */
    void delete(String key);

    /**
     * 根据指定的uri获取与此uri相关的所有缓存key
     * @param uri
     * @return
     */
    Set<String> getKeysForUri(String uri);

}
