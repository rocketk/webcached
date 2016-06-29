package py.webcache.config;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import py.webcache.config.ConfigHelper;
import py.webcache.config.ConfigLoader;
import py.webcache.config.pojo.Trigger;
import py.webcache.exception.NoSuchCacheConfigException;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by pengyu on 2016/5/24.
 */
public class ConfigHelperTest {
    private ConfigHelper configHelper;

    @Before
    public void before() {
        configHelper = new ConfigHelper();
        Assert.assertNotNull(configHelper);
        configHelper.setConfiguration(ConfigLoader.loadConfigFromXml());
        Assert.assertNotNull(configHelper.getConfiguration());
    }

    @Test
//    @Ignore
    public void testGetParamTreeMap() throws Exception {
        Map<String, String[]> map = new HashedMap();
        map.put("tk", new String[]{""}); // 空值，应当被忽略
        map.put("asdf", new String[]{"fdsa"});
        map.put("b", new String[]{"bbb"});
        map.put("a", new String[]{"aaa"});
        /* 测试parameter.type=exclude_all的情况 */
        TreeMap<String, String[]> paramTreeMap = configHelper.getParamTreeMap("/m/sys/office/collectorLocations.htm", map);
        Assert.assertNotNull(paramTreeMap);
        Assert.assertEquals(3, paramTreeMap.size());
//        Iterator it = paramTreeMap.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) it.next();
//        }
        // 验证key的顺序
        Assert.assertTrue(Arrays.equals(new String[]{"a", "asdf", "b"}, paramTreeMap.keySet().toArray()));
        // 验证值
        Assert.assertTrue(Arrays.equals(new String[]{"aaa"}, paramTreeMap.get("a")));
        /* 测试parameter.type=include_all的情况 */
        paramTreeMap = configHelper.getParamTreeMap("/m/stu/userInfo.xhtm", map);
        Assert.assertNotNull(paramTreeMap);
        Assert.assertEquals(1, paramTreeMap.size());
        // 验证key的顺序
        Assert.assertTrue(Arrays.equals(new String[]{"asdf"}, paramTreeMap.keySet().toArray()));
        // 验证值
        Assert.assertTrue(Arrays.equals(new String[]{"fdsa"}, paramTreeMap.get("asdf")));
    }

    @Test
//    @Ignore
    public void testIsSupportCache() throws Exception {
        String uri1 = "/m/sys/office/collectorLocations.htm";
        String uri2 = "/m/stu/userInfo.xhtm";
        String uri3 = "/a/sys/office/save";
        Assert.assertTrue(configHelper.isSupportCache(uri1));//支持缓存
        Assert.assertTrue(configHelper.isSupportCache(uri2));//支持缓存
        Assert.assertTrue(!configHelper.isSupportCache(uri3));//不支持缓存
    }

    @Test
//    @Ignore
    public void testGetExpiredTime() throws Exception {
        String uri1 = "/m/sys/office/collectorLocations.htm";
        String uri2 = "/m/stu/userInfo.xhtm";
        String uri3 = "/a/sys/office/save";
        Assert.assertEquals(180, configHelper.getExpiredTime(uri1));//自定义时长
        Assert.assertEquals(60, configHelper.getExpiredTime(uri2));//默认时长
        //找不到uri，报错
        try {
            configHelper.getExpiredTime(uri3);
            Assert.fail(uri3 + " is actually inexistent, which should call a NoSuchCaccheConfigException here");
        } catch (NoSuchCacheConfigException e) {
            // 正常
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testIsSupportStatus() throws Exception {
        String uri1 = "/m/sys/office/collectorLocations.htm";
        String uri2 = "/m/stu/userInfo.xhtm";
        String uri3 = "/a/sys/office/save";
        Assert.assertTrue(configHelper.isSupportStatus(uri1, 200));
        Assert.assertTrue(!configHelper.isSupportStatus(uri1, 300));
        Assert.assertTrue(configHelper.isSupportStatus(uri2, 200));// 测试默认值
        Assert.assertTrue(!configHelper.isSupportStatus(uri2, 300));// 测试默认值
        //找不到uri，报错
        try {
            configHelper.isSupportStatus(uri3, 200);
            Assert.fail(uri3 + " is actually inexistent, which should call a NoSuchCaccheConfigException here");
        } catch (NoSuchCacheConfigException e) {
            // 正常
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testIsTrigger() throws Exception {
        String uri1 = "/a/sys/office/save";
        String uri2 = "/m/stu/userInfo.xhtm";
        String uri3 = "/a/sys/office/save1";
        Assert.assertTrue(configHelper.isTrigger(uri1));
        Assert.assertFalse(configHelper.isTrigger(uri2));
        Assert.assertFalse(configHelper.isTrigger(uri3));
    }

    @Test
    public void testCachedUriSetForTrigger() throws Exception {

        String uri1 = "/a/sys/office/save";
        String uri2 = "/m/stu/updateStu.xhtm";
        Assert.assertEquals(1, configHelper.cachedUriSetForTrigger(uri1).size());
        Assert.assertEquals(2, configHelper.cachedUriSetForTrigger(uri2).size());
    }

    @Test
    public void testGetTriggerStrategy() throws Exception {
        String uri1 = "/a/sys/office/save";
        String uri2 = "/m/stu/userInfo.xhtm";
        Assert.assertTrue(configHelper.getTrigger(uri1, "/m/sys/office/collectorLocations.htm").getStrategy().equals(Trigger.Strategy.refresh));
        Assert.assertNull(configHelper.getTrigger(uri1, "/m/sys/office/collectorLocations1.htm"));
        Assert.assertNull(configHelper.getTrigger(uri2, "/m/sys/office/collectorLocations.htm"));
    }
}