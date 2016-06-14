package py.webcache.handler;


import py.webcache.util.CollectionUtil;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 缓存对象
 * Created by pengyu on 2016/5/12.
 */
public class DefaultKeyGenerator implements KeyGenerator {

    /**
     * 缓存key前缀
     */
    public static final String KEY_PREFIX = "webcache_";

    /**
     * 生成一个key
     *
     * @param uri
     * @return
     */
    @Override
    public String generateKey(String uri, Map<String, String[]> paramMap, String method) {
        return generateKey(uri, paramMap, method, 250);
    }

    /**
     * 生成一个key，当key的长度超过maxLength时，对其取md5值
     * @param uri
     * @param paramMap
     * @param method
     * @param maxLength
     * @return
     */
    @Override
    public String generateKey(String uri, Map<String, String[]> paramMap, String method, int maxLength) {
        if (uri == null || uri.length() == 0) {
            throw new IllegalArgumentException("uri should not be empty");
        }
        if (method == null || method.length() == 0) {
            method = "GET";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(KEY_PREFIX);
        sb.append(method).append("_");
        String urlString = sb.append(generateUrlStringWithParams(uri, paramMap)).toString();
        // TODO: 2016/6/1 暂时不限定长度，因为会影响getUriFromKey的正常使用
//        if (urlString.length() > maxLength) {
//            urlString = Md5Util.md5(urlString);
//        }
        return urlString;
    }

    @Override
    public String getUriFromKey(String key) {
        if (key == null || key.length() == 0) {
            throw new IllegalArgumentException("key should not be empty");
        }
        String replaced = key.replace(KEY_PREFIX + "GET_", "").replace(KEY_PREFIX + "POST_", "");
        replaced = replaced.substring(0, replaced.indexOf("?") < 0 ? replaced.length() : replaced.indexOf("?"));
        return replaced;
    }
    
    @Override
    public Map<String, String[]> getParamsFromKey(String key) {
        Map<String, String[]> params = new TreeMap<>();
        Pattern pattern = Pattern.compile("(?<=\\?|&)[^?^&]*(?=&||\\b)");
        Matcher matcher = pattern.matcher(key);
        while (matcher.find()) {
            String equation = matcher.group();
            String[] split = equation.split("=");
            if (params.get(split[0]) != null) {
                String[] newValues = Arrays.copyOf(params.get(split[0]), params.get(split[0]).length + 1);
                newValues[newValues.length - 1] = split[1];
                params.put(split[0], newValues);
            } else {
                params.put(split[0], new String[]{split[1]});
            }
        }
        return params;
    }



    private String generateUrlStringWithParams(String uri, Map<String, String[]> paramMap) {
        StringBuilder sb = new StringBuilder();
        sb.append(uri);
        if (paramMap != null && paramMap.size() > 0) {
            sb.append("?");
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                String[] value = entry.getValue();
                if (!CollectionUtil.isStrArrEmpty(value)) {
                    for (String val : value) {
                        if (val != null && val.length() > 0) {
                            sb.append(entry.getKey()).append("=").append(val).append("&");
                        }
                    }
                }
            }
        }
        String urlString = sb.toString();
        if (urlString.endsWith("&")) {
            urlString = urlString.substring(0, urlString.length() - 1);
        }
        return urlString;
    }

}
