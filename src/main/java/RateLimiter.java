import entity.ApiLimit;
import exception.InternalErrorException;
import exception.InvalidUrlException;
import lombok.extern.slf4j.Slf4j;
import strategy.FixedWindowRateLimit;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Fixed time window rate limiter based on memory. This class is thread safe.
 */
@Slf4j
public class RateLimiter extends AbstractRateLimiter implements IRateLimiter {

    //API counter
    private ConcurrentHashMap<String, FixedWindowRateLimit> counters = new ConcurrentHashMap<>();

    @Override
    public boolean limit(String appId, String url) throws InternalErrorException, InvalidUrlException {
        ApiLimit apiLimit = this.rateLimitRule.getLimit(appId, url);
        if (apiLimit == null) {
            return true;
        }
        // get rateLimitCounter for the API
        String counterKey = appId + ":" + apiLimit.getApi();
        FixedWindowRateLimit rateLimitCounter = counters.get(counterKey);
        if (rateLimitCounter == null) {
            FixedWindowRateLimit newRateLimitCounter = new FixedWindowRateLimit(apiLimit);
            rateLimitCounter = counters.putIfAbsent(counterKey, newRateLimitCounter);
            if (rateLimitCounter == null) {
                rateLimitCounter = newRateLimitCounter;
            }
        }
        // limit or not
        return rateLimitCounter.tryAcquire();
    }
}
