package webcache.handler;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import py.webcache.handler.DefaultKeyGenerator;
import py.webcache.handler.KeyGenerator;
import py.webcache.handler.proto.ProtobufKeySetSerializer;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by pengyu on 2016/5/26.
 */
public class KeySetSerializerTest {

    private ProtobufKeySetSerializer protobufKeySetSerializer;
    private KeyGenerator keyGenerator;

    @Before
    public void before() {
        protobufKeySetSerializer = new ProtobufKeySetSerializer();
        keyGenerator = new DefaultKeyGenerator();
    }

    /**
     * 本用例旨在测试两种方式的序列化和反序列化的性能，并验证程序是否正确
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        Set<String> set = createSet();
        byte[] bytes = protobufKeySetSerializer.serialize(set);
        Assert.assertNotNull(bytes);
        Set<String> set2 = protobufKeySetSerializer.deserialize(bytes);
        Assert.assertTrue(CollectionUtils.isEqualCollection(set, set2));
    }

    private Set<String> createSet() {
        Set<String> set = new HashSet<>();
        set.add("/m/asdf/asdfa");
        set.add("/m/asdf/fdsa");
        set.add("/m/asdf/aaa");
        set.add("/m/asdf/fff");
        return set;
    }
}