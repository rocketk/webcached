package py.webcache.web;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.webcache.config.ConfigHelper;
import py.webcache.config.pojo.Condition;
import py.webcache.config.pojo.GlobalSetting;
import py.webcache.config.pojo.Trigger;
import py.webcache.handler.CacheHandler;
import py.webcache.handler.CacheObject;
import py.webcache.handler.KeyGenerator;
import py.webcache.service.CacheRefreshService;
import py.webcache.util.StrExpressionUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * web端缓存过滤器
 * Created by pengyu on 2016/5/12.
 */

public class WebCacheFilter implements Filter {
    private Logger log = LoggerFactory.getLogger(WebCacheFilter.class);
    protected String HEADER_WEBCACHE = "webcache";
    protected ConfigHelper configHelper;
    protected CacheHandler cacheHandler;
    protected CachedContentHttpServletResponseFactory cachedContentHttpServletResponseFactory;
    protected KeyGenerator keyGenerator;
    protected CacheRefreshService cacheRefreshService;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("-------------------------------WebCacheFilter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // TODO untested 流程未测试
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        CachedContentHttpServletResponse responseWrapper = cachedContentHttpServletResponseFactory.getCachedHttpServletResponse(res);
        String method = req.getMethod();
//        String uri = req.getRequestURI();
        String uri = req.getServletPath().replaceAll("//", "/"); // 相当于 req.getRequestURI().replace(req.getContextPath(), "")
        setGlobalSettings(req);
        // 判断该请求是否支持缓存
        if (configHelper.isSupportCache(uri)) { // 支持缓存
            // 对参数进行排序，和去空
            TreeMap<String, String[]> paramMap = configHelper.getParamTreeMap(uri, req.getParameterMap());
            // 判断是否强制更新缓存
            if (!isForceUpdate(req)) {
                String key = keyGenerator.generateKey(uri, paramMap, method);
                CacheObject cacheObject = cacheHandler.getCacheObject(key);
                // 判断是否有缓存
                if (cacheObject != null) { // 有缓存
                    onHitCache(req, responseWrapper, cacheObject);
                    responseWrapper.setContentType(cacheObject.getContentType());
                    responseWrapper.setCharacterEncoding(cacheObject.getCharacterEncoding());
                    responseWrapper.getWriter().println(cacheObject.getContent());
                    return;
                } else {
                    onNoCache(req, res);
                }
            } else {
                onForceUpdate(req, res);
            }
            // 强制更新，或无缓存，在执行完过滤链之后，要把结果缓存起来
            try {
                chain.doFilter(request, responseWrapper);
                if (configHelper.isSupportStatus(uri, responseWrapper.getStatus())) {
                    buildAndPutCacheObject(req, responseWrapper, uri, paramMap, method);
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw e; // 异常直接抛出
            }
            return;
        } else {
            // 不支持缓存
            onSkipCache(req, res);
            chain.doFilter(request, responseWrapper);
            if (responseWrapper.getStatus() == 200) {
                trigger(req);
            }
        }
    }

    /**
     * 判断请求uri是否为触发器trigger，并触发相应的更新缓存操作
     *
     * @param request
     * @throws IOException
     * @throws ServletException
     */
    protected void trigger(HttpServletRequest request) throws IOException, ServletException {
        // TODO untested 流程未测试
        // 判断是否是trigger，如果是，找出它所要更新的全部cacheUri
        String triggerUri = request.getServletPath();
        if (configHelper.isTrigger(triggerUri)) {
            Set<String> configCachedUriList = configHelper.cachedUriSetForTrigger(triggerUri);
            if (configCachedUriList != null) {
                for (String configCachedUri : configCachedUriList) {
                    Set<String> allKeysOfUri = cacheHandler.getKeysForUri(configCachedUri);
                    if (allKeysOfUri != null && allKeysOfUri.size() > 0) {
                        Trigger trigger = configHelper.getTrigger(triggerUri, configCachedUri);
                        if (trigger == null) {
                            continue;
                        }
                        Set<Condition> conditions = configHelper.getConditions(triggerUri, configCachedUri);
                        for (String key : allKeysOfUri) {
                            Map<String, String[]> sortedParams = keyGenerator.getParamsFromKey(key);
                            // 逐条判断缓存服务器中的每一个key是否符合condition
                            if (trigger.getScope().equals(Trigger.Scope.all) ||
                                    matchCondition(configCachedUri, sortedParams, request.getParameterMap(), conditions)) {
                                // 判断更新策略，clear/refresh
                                Trigger.Strategy strategy = trigger.getStrategy();
                                switch (strategy) {
                                    case refresh:
                                        // TODO: 2016/6/6  更新操作未实现，暂时先按clear处理。
                                        cacheHandler.delete(key);
//                                        refreshCache(key, request);
                                        cacheRefreshService.refreshCache(key, request.getServerPort(), request.getContextPath());
                                        break;
                                    case clear:
                                        cacheHandler.delete(key);
                                        break;
                                }
                            }

                        }

                    }

                }
            }

        }
    }

    protected boolean matchCondition(String cachedUri, Map<String, String[]> cachedUrlParams,
                                     Map<String, String[]> triggerUrlParams, Set<Condition> conditions) {
        // TODO: 2016/6/6 判断key是否符合条件，需要考虑表达式的使用
        // 从key中获取参数，从配置文件中获取condition，如果condition中有表达式，则要从request中获取相应的值，来跟key中的参数进行比较
        // 只要满足一个condition，就返回true，当所有condition都不满足时，返回false
        for (Condition condition : conditions) {
            switch (condition.getType()) {
                case nonParam:
                    // 缓存uri中不包含参数的，返回true，否则继续遍历
                    if (cachedUrlParams == null || cachedUrlParams.size() == 0) {
                        return true;
                    }
                    break;
                case paramEqual:
                    List<String> realValueOfCondition = StrExpressionUtil.parseExpressionWithMultiValue(condition.getValue(), triggerUrlParams);
                    String[] values = cachedUrlParams.get(condition.getName());
                    if (CollectionUtils.intersection(realValueOfCondition, Arrays.asList(values)).size() > 0) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    protected boolean isForceUpdate(HttpServletRequest request) {
        // TODO untested 需要结合web来测试
        GlobalSetting globalSetting = configHelper.getGlobalSetting();
        String headerKey = globalSetting.getForceUpdateName();
        String headerValue = globalSetting.getForceUpdateValue();
        if (headerKey == null && headerKey.length() == 0) {
            throw new NullPointerException("the global setting 'forceUpdateName' is empty");
        }
        if (headerValue == null && headerValue.length() == 0) {
            throw new NullPointerException("the global setting 'forceUpdateValue' is empty");
        }
        return headerValue.equals(request.getHeader(headerKey));
    }
    
//    protected void refreshCache(String key, HttpServletRequest request) {
//        // TODO: 2016/6/30 只处理GET类型
//        String url = keyGenerator.getUrlFromKey(key);
//        Map<String, String> headers = new HashMap<>();
//        Configuration configuration = configHelper.getConfiguration();
//        headers.put(configuration.getGlobalSetting().getForceUpdateName(), configuration.getGlobalSetting().getForceUpdateName());
//        StringBuilder sb = new StringBuilder();
//        sb.append("http://127.0.0.1:").append(request.getServerPort()).append("/").append(request.getContextPath()).append("/").append(url);
//        httpClientUtils.httpGet(sb.toString(), headers);
//    }


    @Override
    public void destroy() {
        System.out.println("-------------------------------WebCacheFilter destroy");
    }

    /**
     * 创建一个缓存对象并将其推到缓存服务器上
     * CacheObject
     *
     * @param request
     * @param response
     * @param uri
     * @param paramMap
     * @param method
     * @return
     */
    protected CacheObject buildAndPutCacheObject(HttpServletRequest request, CachedContentHttpServletResponse response,
                                                 String uri, Map<String, String[]> paramMap, String method) {
        CacheObject cacheObject = new CacheObject();
        cacheObject.setKey(keyGenerator.generateKey(uri, paramMap, method));
//        cacheObject.setUri(uri);
//        cacheObject.setParamMap(paramMap);
//        cacheObject.setMethod(method);
        cacheObject.setContent(response.getContentAsString());
        cacheObject.setContentType(response.getContentType());
        cacheObject.setCharacterEncoding(response.getCharacterEncoding());
        cacheObject.setExpiredTime(configHelper.getExpiredTime(uri));
        cacheObject.setPutTime(System.currentTimeMillis());
        cacheHandler.putCacheObject(cacheObject);
        return cacheObject;
    }

    protected void onHitCache(HttpServletRequest request, HttpServletResponse response, CacheObject cacheObject) {
        response.setHeader(HEADER_WEBCACHE, "hit");
        long cost = (System.currentTimeMillis() - cacheObject.getPutTime()) / 1000; // 已消耗了多长时间，秒
        long left = cacheObject.getExpiredTime() - cost; // 还剩多少时间，秒
        response.setHeader("cach_left_time", String.format("%d:%d:%d", left / 3600, left % 3600 / 60, left % 60));
    }

    protected void onSkipCache(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(HEADER_WEBCACHE, "skip");
    }

    protected void onNoCache(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(HEADER_WEBCACHE, "no cache");
    }

    protected void onForceUpdate(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(HEADER_WEBCACHE, "force update");
    }

    protected void setGlobalSettings(HttpServletRequest request) {
        GlobalSetting globalSetting = configHelper.getGlobalSetting();
        if (globalSetting.getContextPath() == null) {
            globalSetting.setContextPath(request.getContextPath());
        }
        if (globalSetting.getPort() == null) {
            globalSetting.setPort(request.getServerPort());
        }
    }

    public ConfigHelper getConfigHelper() {
        return configHelper;
    }

    public void setConfigHelper(ConfigHelper configHelper) {
        this.configHelper = configHelper;
    }

    public CacheHandler getCacheHandler() {
        return cacheHandler;
    }

    public void setCacheHandler(CacheHandler cacheHandler) {
        this.cacheHandler = cacheHandler;
    }

    public CachedContentHttpServletResponseFactory getCachedContentHttpServletResponseFactory() {
        return cachedContentHttpServletResponseFactory;
    }

    public void setCachedContentHttpServletResponseFactory(CachedContentHttpServletResponseFactory cachedContentHttpServletResponseFactory) {
        this.cachedContentHttpServletResponseFactory = cachedContentHttpServletResponseFactory;
    }

    public void setCacheRefreshService(CacheRefreshService cacheRefreshService) {
        this.cacheRefreshService = cacheRefreshService;
    }

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

}
