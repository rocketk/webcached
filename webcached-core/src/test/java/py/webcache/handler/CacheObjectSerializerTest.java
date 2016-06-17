package py.webcache.handler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import py.webcache.handler.CacheObject;
import py.webcache.handler.DefaultKeyGenerator;
import py.webcache.handler.JavaCacheObjectSerializer;
import py.webcache.handler.KeyGenerator;
import py.webcache.handler.proto.ProtobufCacheObjectSerializer;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by pengyu on 2016/5/26.
 */
public class CacheObjectSerializerTest {

    private JavaCacheObjectSerializer javaCacheObjectSerializer;
    private ProtobufCacheObjectSerializer protobufCacheObjectSerializer;
    private KeyGenerator keyGenerator;

    @Before
    public void before() {
        javaCacheObjectSerializer = new JavaCacheObjectSerializer();
        protobufCacheObjectSerializer = new ProtobufCacheObjectSerializer();
        keyGenerator = new DefaultKeyGenerator();
    }

    /**
     * 本用例旨在测试两种方式的序列化和反序列化的性能，并验证程序是否正确
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        CacheObject co = createCacheObjcet(); // 原始对象
        System.out.println("序列化测试开始");
        int loop = 10; // 循环次数
        byte[] bytes1 = null;
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            bytes1 = javaCacheObjectSerializer.serialize(co);
        }
        long t1 = System.currentTimeMillis();
        byte[] bytes2 = null;
        for (int i = 0; i < loop; i++) {
            bytes2 = protobufCacheObjectSerializer.serialize(co);
        }
        long t2 = System.currentTimeMillis();
        System.out.println("序列化测试结束");
        System.out.println(String.format("共循环%d次", loop));
        System.out.println(String.format("采用%s时耗时%d毫秒，平均每次耗时%f毫秒；",
                JavaCacheObjectSerializer.class.getSimpleName(), t1-t0, (t1-t0+0.0d)/loop));
        System.out.println(String.format("采用%s时耗时%d毫秒，平均每次耗时%f毫秒；",
                ProtobufCacheObjectSerializer.class.getSimpleName(), t2-t1, (t2-t1+0.0d)/loop));

        System.out.println("反序列化测试开始");
        long t10 = System.currentTimeMillis();
        CacheObject co1 = null; // 反序列化之后得到的对象
        for (int i = 0; i < loop; i++) {
            co1 = javaCacheObjectSerializer.deserialize(bytes1);
        }
        long t11 = System.currentTimeMillis();
        CacheObject co2 = null; // 反序列化之后得到的对象
        for (int i = 0; i < loop; i++) {
            co2 = protobufCacheObjectSerializer.deserialize(bytes2);
        }
        long t12 = System.currentTimeMillis();
        testEquals(co, co1);
        testEquals(co, co2);
        System.out.println("反序列化测试结束");
        System.out.println(String.format("共循环%d次", loop));
        System.out.println(String.format("采用%s时耗时%d毫秒，平均每次耗时%f毫秒；",
                JavaCacheObjectSerializer.class.getSimpleName(), t11-t10, (t11-t10+0.0d)/loop));
        System.out.println(String.format("采用%s时耗时%d毫秒，平均每次耗时%f毫秒；",
                ProtobufCacheObjectSerializer.class.getSimpleName(), t12-t11, (t12-t11+0.0d)/loop));

    }

    private String createKey() {
        String uri = "/m/sys/office/collectorLocations.htm";
        String method = "GET";
        Map<String, String[]> paramMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        paramMap.put("b", new String[]{"b1"});
        paramMap.put("a", new String[]{"abc"});
        paramMap.put("aa", new String[]{"abc", "aaabc"});
        paramMap.put("t", new String[]{"tf"});
        return keyGenerator.generateKey(uri, paramMap, method);
    }

    private CacheObject createCacheObjcet() {
        String key = createKey();
        CacheObject co = new CacheObject();
        co.setKey(key);
        co.setContent("adsfadfadfadfadfafdada");
        co.setContentType("json");
        co.setCharacterEncoding("utf-8");
        co.setPutTime(System.currentTimeMillis() / 1000);
        co.setCost(3600);
        co.setLeft(3600);
        co.setExpiredTime(7200);
        return co;
    }

    private void testEquals(CacheObject a, CacheObject b) {
        Assert.assertNotNull(a);
        Assert.assertNotNull(b);
        Assert.assertEquals(a.getKey(), b.getKey());
        Assert.assertEquals(a.getContent(), b.getContent());
        Assert.assertEquals(a.getCharacterEncoding(), b.getCharacterEncoding());
        Assert.assertEquals(a.getContentType(), b.getContentType());
        Assert.assertEquals(a.getCost(), b.getCost());
        Assert.assertEquals(a.getLeft(), b.getLeft());
        Assert.assertEquals(a.getExpiredTime(), b.getExpiredTime());
        Assert.assertEquals(a.getPutTime(), b.getPutTime());
    }

}