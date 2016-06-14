package py.webcache.exception;

/**
 * Created by pengyu on 2016/5/25.
 */
public class NoSuchCacheConfigException extends RuntimeException {
    public NoSuchCacheConfigException() {
    }

    public NoSuchCacheConfigException(String message) {
        super(message);
    }

    public NoSuchCacheConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchCacheConfigException(Throwable cause) {
        super(cause);
    }

    public NoSuchCacheConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
