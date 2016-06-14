package webcache.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import py.webcache.config.pojo.CacheConfig;
import py.webcache.util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
}