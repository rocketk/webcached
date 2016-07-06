package py.webcache.service;

import py.webcache.config.ConfigHelper;
import py.webcache.config.pojo.Configuration;
import py.webcache.handler.CacheHandler;
import py.webcache.handler.KeyGenerator;
import py.webcache.util.HttpClientUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 刷新缓存
 * Created by pengyu on 2016/7/6.
 */
public class CacheRefreshService {
    protected KeyGenerator keyGenerator;
    protected ConfigHelper configHelper;
    protected HttpClientUtils httpClientUtils;

    public void refreshCache(String key, int port, String contextPath) {
        // TODO: 2016/6/30 只处理GET类型
        String url = keyGenerator.getUrlFromKey(key);
        Map<String, String> headers = new HashMap<>();
        Configuration configuration = configHelper.getConfiguration();
        headers.put(configuration.getGlobalSetting().getForceUpdateName(), configuration.getGlobalSetting().getForceUpdateValue());
        StringBuilder sb = new StringBuilder();
        sb.append("http://127.0.0.1:").append(port).append("/").append(contextPath).append("/").append(url);
        System.out.println(sb.toString());
        System.out.println(httpClientUtils.httpGet(sb.toString(), headers));
    }

    public void setHttpClientUtils(HttpClientUtils httpClientUtils) {
        this.httpClientUtils = httpClientUtils;
    }

    public void setConfigHelper(ConfigHelper configHelper) {
        this.configHelper = configHelper;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }
}
