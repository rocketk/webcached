package py.webcache.config;

import py.webcache.config.pojo.Configuration;

/**
 * 工厂
 * Created by pengyu on 2016/5/26.
 */
public class ConfigFactory {
    private String configLocation;
    private Configuration configuration;

    public Configuration getConfiguration() {
        if (configuration == null) {
            synchronized (this) {
                configuration = ConfigLoader.loadConfigFromXml(configLocation);
            }
        }
        return configuration;
    }
}
