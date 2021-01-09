package strategy;

import exception.InternalErrorException;

public interface IRateLimitStrategy {
    /**
     * try to acquire an access token.
     *
     * @return true if get an access token successfully, otherwise, return false.
     * @throws InternalErrorException if some internal error occurs.
     */
    boolean tryAcquire() throws InternalErrorException;

}
