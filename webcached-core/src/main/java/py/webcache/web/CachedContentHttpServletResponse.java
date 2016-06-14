package py.webcache.web;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by pengyu on 2016/5/13.
 */
public interface CachedContentHttpServletResponse extends HttpServletResponse {
    String getContentAsString();
    int getStatus();
}
