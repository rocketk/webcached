package py.webcache.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import py.webcache.config.pojo.CacheConfig;
import py.webcache.util.CollectionUtil;

import java.util.*;

/**
 * Created by pengyu on 2016/5/19.
 */
public class CollectionUtilTest {

    @Test
    public void testArrayToSetAsInteger() throws Exception {
        String str = "200,302";
        Set<Integer> set = CollectionUtil.arrayToSetAsInteger(str);
        Assert.assertNotNull(set);
        Assert.assertEquals(2, set.size());
        Assert.assertTrue(set.contains(200));
        Assert.assertTrue(set.contains(302));
    }

    @Test
//    @Ignore
    public void testArrayToSetAsString() throws Exception {
        String str = "200,302";
        Set<String> set = CollectionUtil.arrayToSetAsString(str);
        Assert.assertNotNull(set);
        Assert.assertEquals(2, set.size());
        Assert.assertTrue(set.contains("200"));
        Assert.assertTrue(set.contains("302"));
    }

    @Test
//    @Ignore
    public void testListToMap() throws Exception {
        List<CacheConfig> list = new ArrayList<>();
        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.setUri("/abcd");
        list.add(cacheConfig);
        cacheConfig = new CacheConfig();
        cacheConfig.setUri("/ffff");
        list.add(cacheConfig);
        Map<String, CacheConfig> map = CollectionUtil.listToMap(list, "uri", String.class);
        Assert.assertNotNull(map);
        Assert.assertEquals(2, map.size());
        Assert.assertEquals("/abcd", map.get("/abcd").getUri());
    }

    @Test
    public void testTravers() throws Exception {
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
        CollectionUtil.travers(paramMap, new CollectionUtil.Travelers<String, String[]>() {
            @Override
            public void dosomething(String s, String[] strings, Object o) {

            }
        });
    }
}