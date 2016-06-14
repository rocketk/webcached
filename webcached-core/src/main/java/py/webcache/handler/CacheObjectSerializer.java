package py.webcache.handler;

/**
 * 序列化处理器接口，用于序列化缓存对象成字节数组
 * Created by pengyu on 2016/5/25.
 */
public interface CacheObjectSerializer {
    byte[] serialize(CacheObject cacheObject);

    CacheObject deserialize(byte[] bytes);
}
