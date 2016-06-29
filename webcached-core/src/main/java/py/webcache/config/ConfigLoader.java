package py.webcache.config;


import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.webcache.config.pojo.*;
import py.webcache.exception.LoadConfigurationException;
import py.webcache.util.CollectionUtil;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 配置对象加载器
 * Created by pengyu on 2016/5/12.
 */
public class ConfigLoader {
    protected static Logger log = LoggerFactory.getLogger(ConfigLoader.class);
    protected static final String DEFAUL_XML = "webcached.xml";
//    protected static final String GLOBAL_SETTING = "global-setting";
//    protected static final String CACHES = "caches";
//    protected static final String UPDATE_TRIGGERS = "update-triggers";
//
//    protected static final String CACHE = "cache";

    public static Configuration loadConfigFromXml(String xmlName) {
        if (xmlName == null || xmlName.length() == 0) {
            throw new IllegalArgumentException("xmlName should not be empty.");
        }
        Configuration configuration = null;
        SAXReader saxReader = new SAXReader();
        try {
            InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream(xmlName);
            Document document = saxReader.read(is);
            Element root = document.getRootElement();
            configuration = new Configuration();
            // global-setting
            List<Element> globalSettingElements = root.element("global-setting").elements();
            configuration.setGlobalSetting(parseGlobalSetting(globalSettingElements));
            // caches
            List<Element> cacheElements = root.element("caches").elements();
            configuration.setCaches(parseCacheConfigs(cacheElements));
            List<Element> triggerElements = root.element("update-triggers").elements();
            configuration.setTriggers(parseTriggers(triggerElements));
            configuration.setTriggersGroupByCacheUri(CollectionUtil.groupSetToMap(configuration.getTriggers(), "cacheUri", String.class));
            configuration.setTriggersGroupByTriggerUri(CollectionUtil.groupSetToMap(configuration.getTriggers(), "triggerUri", String.class));
            configuration.setTriggersGroupById(CollectionUtil.groupSetToUniqueMap(configuration.getTriggers(), "id", Trigger.Id.class));
            if (configuration.getTriggers() == null) {
                configuration.setTriggers(new HashSet<Trigger>());
                configuration.setTriggersGroupByCacheUri(new HashMap<String, Set<Trigger>>());
                configuration.setTriggersGroupByTriggerUri(new HashMap<String, Set<Trigger>>());
                configuration.setTriggersGroupById(new HashMap<Trigger.Id, Trigger>());
            }
        } catch (DocumentException e) {
            throw new LoadConfigurationException("An exception occurred when reading " + xmlName, e);
        } catch (IllegalAccessException e) {
            throw new LoadConfigurationException("An exception occurred when reading " + xmlName, e);
        } catch (NoSuchFieldException e) {
            throw new LoadConfigurationException("An exception occurred when reading " + xmlName, e);
        }
        return configuration;
    }

    public static Configuration loadConfigFromXml() {
        return loadConfigFromXml(DEFAUL_XML);
    }

    /**
     * 获得属性值，依次从attributes和elements中去找，如果attributes中已经有了，就直接返回
     *
     * @param e
     * @param name
     * @return
     */
    protected static String getPropertyText(Element e, String name) {
        // find in attributes
        Attribute attr = e.attribute(name);
        if (attr != null && attr.getText() != null) {
            return attr.getText().trim();
        }
        // find in element
        Element ee = e.element(name);
        if (ee != null) {
            return ee.getTextTrim();
        }
        return null;
    }

    protected static CacheConfig parseCacheConfig(Element e) {
        if (e == null) {
            return null;
        }
        CacheConfig cacheConfig = new CacheConfig();
        String property = getPropertyText(e, "uri");
        if (property != null && property.length() > 0) {
            cacheConfig.setUri(property);
        }
        property = getPropertyText(e, "auto-refresh");
        if (property != null && property.length() > 0) {
            cacheConfig.setAutoRefresh(Boolean.parseBoolean(property));
        }
        property = getPropertyText(e, "expired-time");
        if (property != null && property.length() > 0) {
            cacheConfig.setExpiredTime(Integer.parseInt(property));
        }
        property = getPropertyText(e, "support-status");
        Set<Integer> statusSet = CollectionUtil.arrayToSetAsInteger(property);
        if (statusSet != null && statusSet.size() > 0) {
            cacheConfig.setSupportStatus(statusSet);
        }
        Element parameterElement = e.element("parameter");
        if (parameterElement != null) {
            Parameter parameter = parseParameter(parameterElement);
            cacheConfig.setParameter(parameter);
        }
        return cacheConfig;
    }

    protected static Map<String, CacheConfig> parseCacheConfigs(List<Element> cacheElements) throws NoSuchFieldException, IllegalAccessException {
        if (cacheElements != null && cacheElements.size() > 0) {
            List<CacheConfig> cacheConfigList = new ArrayList<>();
            for (Element cachConfigElement : cacheElements) {
                CacheConfig cacheConfig = parseCacheConfig(cachConfigElement);
                cacheConfigList.add(cacheConfig);
            }
            return CollectionUtil.listToMap(cacheConfigList, "uri", String.class);
        }
        return null;
    }

    protected static Parameter parseParameter(Element e) {
        if (e == null) {
            return null;
        }
        Parameter parameter = new Parameter();
        String property = getPropertyText(e, "type");
        if (property != null) {
            if ("exclude_all".equals(property)) {
                parameter.setType(Parameter.Type.excludeAll);
            } else if ("include_all".equals(property)) {
                parameter.setType(Parameter.Type.includeAll);
            } else {
                throw new LoadConfigurationException(String.format("An exception occurred when parsing a parameter, " +
                        "'%s' is invalid for attribute 'type'", property));
            }
        }
        property = getPropertyText(e, "exceptions");
        Set<String> exceptionSet = CollectionUtil.arrayToSetAsString(property);
        if (exceptionSet != null) {
            parameter.setExceptions(exceptionSet);
        }
        return parameter;
    }

    protected static GlobalSetting parseGlobalSetting(List<Element> settingElements) {
        GlobalSetting globalSetting = new GlobalSetting();
        if (settingElements != null && settingElements.size() > 0) {
            for (Element e : settingElements) {
                String value = e.getTextTrim();
                if (value != null && value.length() > 0) {
                    try {
                        Field field = GlobalSetting.class.getDeclaredField(e.getName());
                        field.setAccessible(true);
                        field.set(globalSetting, e.getTextTrim());
                    } catch (IllegalAccessException e1) {
                        log.warn(String.format("field '%s' is unaccessible in %s", e.getName(), GlobalSetting.class));
                        continue;
                    } catch (NoSuchFieldException e1) {
                        log.warn(String.format("no such field '%s' in %s", e.getName(), GlobalSetting.class));
                        continue;
                    }
                }
            }
        }
        return globalSetting;
    }

    protected static Set<Trigger> parseTriggers(List<Element> triggerElements) {
        if (triggerElements != null && triggerElements.size() > 0) {
            Set<Trigger> set = new HashSet<>();
            for (Element e : triggerElements) {
                set.add(parseTrigger(e));
            }
            return set;
        }
        return null;
    }

    protected static Trigger parseTrigger(Element e) {
        if (e == null) {
            return null;
        }
        Trigger trigger = new Trigger();
        String property = getPropertyText(e, "cache-uri");
        if (property != null) {
            trigger.setCacheUri(property);
        }
        property = getPropertyText(e, "trigger-uri");
        if (property != null) {
            trigger.setTriggerUri(property);
        }
        property = getPropertyText(e, "scope");
        if (property != null) {
            if ("specific".equals(property)) {
                trigger.setScope(Trigger.Scope.specific);
            } else if ("all".equals(property)) {
                trigger.setScope(Trigger.Scope.all);
            } else {
                throw new LoadConfigurationException(String.format("An exception occurred when parsing a trigger, " +
                                "'%s' is invalid for attribute 'scope'", property));
            }
        }
        property = getPropertyText(e, "strategy");
        if (property != null) {
            if ("refresh".equals(property)) {
                trigger.setStrategy(Trigger.Strategy.refresh);
            } else if ("clear".equals(property)) {
                trigger.setStrategy(Trigger.Strategy.clear);
            } else {
                throw new LoadConfigurationException(String.format("An exception occurred when parsing a trigger, " +
                                "'%s' is invalid for attribute 'strategy'", property));
            }
        }
        Element conditionsElement = e.element("conditions");
        if (conditionsElement != null) {
            Set<Condition> conditions = parseConditions(conditionsElement.elements());
            trigger.setConditions(conditions);
        }
        return trigger;
    }

    protected static Set<Condition> parseConditions(List<Element> conditionElements) {
        if (conditionElements != null && conditionElements.size() > 0) {
            Set<Condition> set = new HashSet<>();
            for (Element e : conditionElements) {
                set.add(parseCondition(e));
            }
            return set;
        }
        return null;
    }

    protected static Condition parseCondition(Element e) {
        if (e == null) {
            return null;
        }
        Condition condition = new Condition();
        String property = getPropertyText(e, "type");
        if (property != null) {
            if ("param-equal".equals(property)) {
                condition.setType(Condition.Type.paramEqual);
            } else if ("non-param".equals(property)) {
                condition.setType(Condition.Type.nonParam);
            } else {
                throw new LoadConfigurationException(String.format("An exception occurred when parsing a condition, " +
                        "'%s' is invalid for attribute 'type'", property));
            }
        }
        property = getPropertyText(e, "name");
        if (property != null) {
            condition.setName(property);
        }
        property = getPropertyText(e, "value");
        if (property != null) {
            condition.setValue(property);
        }
        return condition;
    }

}
