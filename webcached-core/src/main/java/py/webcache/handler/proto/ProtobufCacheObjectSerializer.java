package py.webcache.handler.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import py.webcache.exception.DeserializationException;
import py.webcache.handler.CacheObject;
import py.webcache.handler.CacheObjectSerializer;

/**
 * 采用Protobuf的方式对缓存对象进行序列化和反序列化
 * Created by pengyu on 2016/5/25.
 */
public class ProtobufCacheObjectSerializer implements CacheObjectSerializer {
    @Override
    public byte[] serialize(CacheObject cacheObject) {
        if (cacheObject == null) {
            throw new NullPointerException("cacheObject should not be null");
        }
        CacheObjectProtocol.CacheObjectProto.Builder builder = CacheObjectProtocol.CacheObjectProto.newBuilder();
        builder.setKey(cacheObject.getKey());
        builder.setContent(cacheObject.getContent());
        builder.setContentType(cacheObject.getContentType());
        builder.setCharacterEncoding(cacheObject.getCharacterEncoding());
        builder.setCost(cacheObject.getCost());
        builder.setExpiredTime(cacheObject.getExpiredTime());
        builder.setPutTime(cacheObject.getPutTime());
        builder.setLeft(cacheObject.getLeft());
        return builder.build().toByteArray();
    }

    @Override
    public CacheObject deserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new NullPointerException("bytes should not be null or empty");
        }
        CacheObjectProtocol.CacheObjectProto.Builder builder = CacheObjectProtocol.CacheObjectProto.newBuilder();
        try {
            CacheObjectProtocol.CacheObjectProto cacheObjectProto = builder.mergeFrom(bytes).build();
            CacheObject cacheObject = new CacheObject();
            cacheObject.setKey(builder.getKey());
            cacheObject.setContent(builder.getContent());
            cacheObject.setContentType(builder.getContentType());
            cacheObject.setCharacterEncoding(builder.getCharacterEncoding());
            cacheObject.setCost(builder.getCost());
            cacheObject.setExpiredTime(builder.getExpiredTime());
            cacheObject.setPutTime(builder.getPutTime());
            cacheObject.setLeft(builder.getLeft());
            return cacheObject;
        } catch (InvalidProtocolBufferException e) {
            throw new DeserializationException("an error occurred when deserializing a CacheObject from a byte array", e);
        }
    }

}
