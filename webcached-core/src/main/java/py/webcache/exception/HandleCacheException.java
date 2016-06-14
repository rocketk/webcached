package py.webcache.exception;

/**
 * Created by pengyu on 2016/5/25.
 */
public class HandleCacheException extends RuntimeException {
    public HandleCacheException() {
    }

    public HandleCacheException(String message) {
        super(message);
    }

    public HandleCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandleCacheException(Throwable cause) {
        super(cause);
    }

    public HandleCacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
