package py.webcache.handler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import py.webcache.handler.DefaultKeyGenerator;
import py.webcache.handler.KeyGenerator;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by pengyu on 2016/5/25.
 */
public class DefaultKeyGeneratorTest {
    private KeyGenerator keyGenerator;

    @Before
    public void before() {
        keyGenerator = new DefaultKeyGenerator();
    }
    @Test
//    @Ignore
    public void testGenerateKey() throws Exception {
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
        String url = keyGenerator.generateKey(uri, paramMap, method);
        System.out.println(url);
        Assert.assertEquals(DefaultKeyGenerator.KEY_PREFIX + "GET_/m/sys/office/collectorLocations.htm?a=abc&aa=abc&aa=aaabc&b=b1&t=tf", url);
        Assert.assertEquals(DefaultKeyGenerator.KEY_PREFIX + "GET_/m/sys/office/collectorLocations.htm", keyGenerator.generateKey(uri, new TreeMap<String, String[]>(), method));
    }

    @Test
    public void testGetUriFromKey() {
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
        String key = keyGenerator.generateKey(uri, paramMap, method);
        System.out.println(key);
        String uri2 = keyGenerator.getUriFromKey(key);
        System.out.println(uri2);
        Assert.assertEquals(uri2, uri);
    }


    @Test
    public void testGetParamsFromKey() throws Exception {
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
        String key =  keyGenerator.generateKey(uri, paramMap, method);
        System.out.println(key);
        Map<String, String[]> paramsFromKey = keyGenerator.getParamsFromKey(key);
        Assert.assertEquals(paramMap.size(), paramsFromKey.size());
    }

    @Test
    public void testGetUrlFromKey() throws Exception {
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
        String key = keyGenerator.generateKey(uri, paramMap, method);
        System.out.println(key);
        String url = keyGenerator.getUrlFromKey(key);
        System.out.println(url);
        Assert.assertEquals("/m/sys/office/collectorLocations.htm?a=abc&aa=abc&aa=aaabc&b=b1&t=tf", url);
    }
}