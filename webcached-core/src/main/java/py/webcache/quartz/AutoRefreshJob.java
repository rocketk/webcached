package py.webcache.quartz;

import py.webcache.config.ConfigHelper;
import py.webcache.config.pojo.CacheConfig;
import py.webcache.config.pojo.Configuration;
import py.webcache.config.pojo.GlobalSetting;
import py.webcache.handler.CacheHandler;
import py.webcache.handler.CacheObject;
import py.webcache.service.CacheRefreshService;

import java.util.Map;

/**
 * Created by pengyu on 2016/7/6.
 */
public class AutoRefreshJob {
    private CacheRefreshService cacheRefreshService;
    private ConfigHelper configHelper;
    private Configuration configuration;
    private CacheHandler cacheHandler;
    private int aheadTime; // 提前量，单位秒，在缓存所剩余时间小于此值时，刷新缓存

    public void execute() {
        System.out.print(".");
        configuration = configHelper.getConfiguration();
        Map<String, CacheConfig> caches = configuration.getCaches();
        GlobalSetting globalSetting = configHelper.getGlobalSetting();
        for (Map.Entry<String, CacheConfig> entry : caches.entrySet()) {
            CacheConfig cacheConfig = entry.getValue();
            if (cacheConfig.isAutoRefresh()) {
                String uri = cacheConfig.getUri();
                for (String key : cacheHandler.getKeysForUri(uri)) {
                    // TODO: 2016/7/6 判断是否缓存失效
                    CacheObject cacheObject = cacheHandler.getCacheObject(key);
                    if (cacheConfig != null) {
                        long cost = (System.currentTimeMillis() - cacheObject.getPutTime()) / 1000; // 已消耗了多长时间，秒
                        long left = cacheObject.getExpiredTime() - cost; // 还剩多少时间，秒
                        if (left <= aheadTime) {
                            System.out.println(String.format("refresh cache, uri: %s, cost: %d, left: %d", cacheObject.getKey(), cost, left));
                            cacheRefreshService.refreshCache(key, globalSetting.getPort(), globalSetting.getContextPath());
                        }
                    }
                }
            }
        }
    }

    public void setCacheRefreshService(CacheRefreshService cacheRefreshService) {
        this.cacheRefreshService = cacheRefreshService;
    }

    public void setConfigHelper(ConfigHelper configHelper) {
        this.configHelper = configHelper;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setCacheHandler(CacheHandler cacheHandler) {
        this.cacheHandler = cacheHandler;
    }

    public void setAheadTime(int aheadTime) {
        this.aheadTime = aheadTime;
    }
}
