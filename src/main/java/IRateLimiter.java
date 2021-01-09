import exception.InternalErrorException;
import exception.InvalidUrlException;
import exception.OverloadException;

public interface IRateLimiter {
    /**
     * check if the url request of the specified app exceeds the max hit limit.
     *
     * @param appId the app ID
     * @param url   the request url
     * @throws OverloadException      if the app exceeds the max hit limit for the api.
     * @throws InvalidUrlException    if the url is invalid.
     * @throws InternalErrorException if some internal error occurs.
     */
    boolean limit(String appId, String url) throws OverloadException, InvalidUrlException, InternalErrorException;
}
