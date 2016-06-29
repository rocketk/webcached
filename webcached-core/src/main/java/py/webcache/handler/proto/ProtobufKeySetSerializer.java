package py.webcache.handler.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import py.webcache.exception.DeserializationException;
import py.webcache.handler.KeySetSerializer;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by pengyu on 2016/6/1.
 */
public class ProtobufKeySetSerializer implements KeySetSerializer {
    @Override
    public byte[] serialize(Set<String> keys) {
        if (keys == null) {
            throw new NullPointerException("keys should not be null");
        }
        KeySetProtocol.KeySetProto.Builder builder = KeySetProtocol.KeySetProto.newBuilder();
        builder.addAllKeys(keys);
        return builder.build().toByteArray();
    }

    @Override
    public Set<String> deserialize(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes should not be null");
        }
        KeySetProtocol.KeySetProto.Builder builder = KeySetProtocol.KeySetProto.newBuilder();
        try {
            builder.mergeFrom(bytes);
            Set<String> keySet = new HashSet<>();
            keySet.addAll(builder.getKeysList());
            return keySet;
        } catch (InvalidProtocolBufferException e) {
            throw new DeserializationException("an error occurred when deserializing a KeySet from a byte array", e);
        }
    }
}
