package py.webcache.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import py.webcache.config.pojo.Configuration;

/**
 * 配置对象工厂，用来在spring环境中生成配置对象的
 * Created by pengyu on 2016/5/26.
 */
public class ConfigSpringFactory implements FactoryBean<Configuration> {
    private Logger log = LoggerFactory.getLogger(ConfigSpringFactory.class);
    private String configLocation;
    @Override
    public Configuration getObject() throws Exception {
        if (configLocation == null || configLocation.length() == 0) {
            return ConfigLoader.loadConfigFromXml();
        }
        return ConfigLoader.loadConfigFromXml(configLocation);
    }

    @Override
    public Class<?> getObjectType() {
        return Configuration.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }
}
