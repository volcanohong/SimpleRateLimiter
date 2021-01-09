package exception;

public class UnsupportedMethodException extends RuntimeException {
    private static final long serialVersionUID = -7437816764933736472L;

    public UnsupportedMethodException(String message) {
        super(message);
    }

    public UnsupportedMethodException(String message, Throwable e) {
        super(message, e);
    }

}
