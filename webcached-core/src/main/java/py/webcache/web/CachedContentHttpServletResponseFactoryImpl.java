package py.webcache.web;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by pengyu on 2016/5/25.
 */
public class CachedContentHttpServletResponseFactoryImpl implements CachedContentHttpServletResponseFactory {
    @Override
    public CachedContentHttpServletResponse getCachedHttpServletResponse(HttpServletResponse res) {
        return new CachedContentResponseWrapper(res, true);
    }
}
