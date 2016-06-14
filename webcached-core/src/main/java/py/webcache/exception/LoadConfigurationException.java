package py.webcache.exception;

/**
 * Created by pengyu on 2016/5/18.
 */
public class LoadConfigurationException extends RuntimeException {
    public LoadConfigurationException() {
        super();
    }

    public LoadConfigurationException(String message) {
        super(message);
    }

    public LoadConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadConfigurationException(Throwable cause) {
        super(cause);
    }

    protected LoadConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
