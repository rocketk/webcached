package py.webcache.web;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by pengyu on 2016/5/25.
 */
public interface CachedContentHttpServletResponseFactory {
    CachedContentHttpServletResponse getCachedHttpServletResponse(HttpServletResponse res);
}
