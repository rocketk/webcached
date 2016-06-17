package py.webcache.config;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import py.webcache.config.ConfigLoader;
import py.webcache.config.pojo.*;

import java.util.Map;
import java.util.Set;

/**
 * Created by pengyu on 2016/5/19.
 */
public class ConfigLoaderTest {

    @Test
//    @Ignore
    public void testLoadConfigFromXml() throws Exception {
        Configuration configuration = ConfigLoader.loadConfigFromXml("webcached.xml");
        Assert.assertNotNull(configuration);
        testParseCaches(configuration.getCaches());
        testParseGlobalSetting(configuration.getGlobalSetting());
        testParseTriggers(configuration.getTriggers());
        testGroupTriggerSet(configuration.getTriggersGroupByCacheUri(), configuration.getTriggersGroupByTriggerUri(), configuration.getTriggersGroupById());
        System.out.println(JSON.toJSONString(configuration));
    }

    private void testParseCaches(Map<String, CacheConfig> caches) {
        Assert.assertNotNull(caches);
        Assert.assertTrue(caches.size() > 0);
        Assert.assertNotNull(caches.get("/m/sys/office/collectorLocations.htm").getParameter());
        Assert.assertTrue(caches.get("/m/sys/office/collectorLocations.htm").getParameter().getExceptions().contains("areaId"));
        Assert.assertEquals(Parameter.Type.excludeAll, caches.get("/m/sys/office/collectorLocations.htm").getParameter().getType());
    }

    private void testParseGlobalSetting(GlobalSetting globalSetting) {
        Assert.assertNotNull(globalSetting);
        Assert.assertEquals("force_update", globalSetting.getForceUpdateName());
        Assert.assertEquals("xhmobile-!@#", globalSetting.getForceUpdateValue());
    }

    private void testParseTriggers(Set<Trigger> triggers) {
        Assert.assertNotNull(triggers);
        Assert.assertNotEquals(0, triggers.size());
        for (Trigger trigger : triggers) {
            testParseTrigger(trigger);
        }
    }
    private void testParseTrigger(Trigger trigger) {
        Assert.assertNotNull(trigger);
        Assert.assertNotNull(trigger.getCacheUri());
        Assert.assertNotNull(trigger.getConditions());
        Assert.assertNotNull(trigger.getScope());
        Assert.assertNotNull(trigger.getStrategy());
        Assert.assertNotNull(trigger.getTriggerUri());
        testParseConditions(trigger.getConditions());
    }
    private void testParseConditions(Set<Condition> conditions) {
        Assert.assertNotNull(conditions);
        Assert.assertNotEquals(0, conditions.size());
        for (Condition condition : conditions) {
            testParseCondition(condition);
        }
    }
    private void testParseCondition(Condition condition) {
        Assert.assertNotNull(condition);
        Assert.assertNotNull(condition.getType());
        if (condition.getType().equals(Condition.Type.paramEqual)) {
            Assert.assertNotNull(condition.getName());
            Assert.assertNotNull(condition.getValue());
        }
    }
    private void testGroupTriggerSet(Map<String, Set<Trigger>> triggersGroupByCacheUri,
                                     Map<String, Set<Trigger>> triggersGroupByTriggerUri,
                                     Map<Trigger.Id, Trigger> triggersGroupById) {
        Assert.assertNotNull(triggersGroupByCacheUri);
        Assert.assertEquals(3, triggersGroupByCacheUri.get("/m/stu/userInfo.xhtm").size());
        Assert.assertEquals(2, triggersGroupByTriggerUri.get("/m/stu/updateStu.xhtm").size());
        Assert.assertEquals(1, triggersGroupByTriggerUri.get("/m/stu/updateUserAvatar.xhtm").size());
        Assert.assertEquals(1, triggersGroupByTriggerUri.get("/m/stu/updateArchives.xhtm").size());
        Assert.assertNotNull(triggersGroupById.get(new Trigger.Id("/m/stu/userInfo.xhtm", "/m/stu/updateStu.xhtm")));
        Assert.assertNull(triggersGroupById.get(new Trigger.Id("/m/stu/userInfo.xhtm", "/m/stu/updateStu111.xhtm")));
    }
}