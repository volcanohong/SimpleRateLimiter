package exception;

public class ConfigurationResolveException extends Exception {

    private static final long serialVersionUID = 8698852953618381051L;

    public ConfigurationResolveException(String message) {
        super(message);
    }

    public ConfigurationResolveException(String message, Throwable e) {
        super(message, e);
    }
}
