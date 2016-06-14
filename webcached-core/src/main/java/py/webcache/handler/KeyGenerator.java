package py.webcache.handler;

import java.util.Map;

/**
 * Created by pengyu on 2016/5/25.
 */
public interface KeyGenerator {
    String generateKey(String uri, Map<String, String[]> paramMap, String method);
    String generateKey(String uri, Map<String, String[]> paramMap, String method, int maxLength);
    String getUriFromKey(String key);

    Map<String, String[]> getParamsFromKey(String key);
}
