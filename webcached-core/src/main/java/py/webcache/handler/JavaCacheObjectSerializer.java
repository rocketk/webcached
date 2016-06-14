package py.webcache.handler;

import py.webcache.exception.DeserializationException;
import py.webcache.exception.SerializationException;

import java.io.*;

/**
 * 采用java原生的方式对缓存对象进行序列化和反序列化
 * Created by pengyu on 2016/5/26.
 */
public class JavaCacheObjectSerializer implements CacheObjectSerializer {
    @Override
    public byte[] serialize(CacheObject cacheObject) {
        if (cacheObject == null) {
            throw new NullPointerException("cacheObject should not be null");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(cacheObject);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("an error occurred when serializing a CacheObject to a byte array", e);
        }
    }

    @Override
    public CacheObject deserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new NullPointerException("bytes should not be null or empty");
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream oi = null;
        try {
            oi = new ObjectInputStream(bais);
            CacheObject cacheObject = (CacheObject) oi.readObject();
            return cacheObject;
        } catch (IOException e) {
            throw new DeserializationException("an error occurred when deserializing a CacheObject from a byte array", e);
        } catch (ClassNotFoundException e) {
            throw new DeserializationException("an error occurred when deserializing a CacheObject from a byte array", e);
        } finally {
            if (oi != null) {
                try {
                    oi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
