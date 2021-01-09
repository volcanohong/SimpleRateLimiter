package strategy;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Stopwatch;
import exception.InternalErrorException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FixedWindowRateLimit implements IRateLimitStrategy {
    /* timeout for {@code Lock.tryLock() }. */
    private static final long TRY_LOCK_TIMEOUT = 200l; // 200ms.
    private Stopwatch stopwatch;
    private AtomicInteger currentCount = new AtomicInteger(0);
    private Lock lock = new ReentrantLock();

    /* the max permitted access count per second */
    private final int limit;


    public FixedWindowRateLimit(int limit) {
        this(limit, Stopwatch.createStarted());
    }

    @VisibleForTesting
    protected FixedWindowRateLimit(int limit, Stopwatch stopwatch) {
        this.limit = limit;
        this.stopwatch = stopwatch;
    }

    @Override
    public boolean tryAcquire() throws InternalErrorException {
        int updatedCount = currentCount.incrementAndGet();
        if (updatedCount <= limit) {
            return true;
        }
        try {
            if (lock.tryLock(TRY_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
                try {
                    if (stopwatch.elapsed(TimeUnit.MILLISECONDS) > TimeUnit.SECONDS.toMillis(1)) {
                        currentCount.set(0);
                        stopwatch.reset();
                    }
                    updatedCount = currentCount.incrementAndGet();
                    return updatedCount <= limit;
                } finally {
                    lock.unlock();
                }
            } else {
                throw new InternalErrorException("tryAcquire() wait lock too long:" + TRY_LOCK_TIMEOUT + "ms");
            }
        } catch (InterruptedException e) {
            throw new InternalErrorException("tryAcquire() is interrupted by lock-time-out.", e);
        }
    }
}
