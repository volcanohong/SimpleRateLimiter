import exception.InternalErrorException;
import exception.InvalidUrlException;
import exception.OverloadException;
import org.junit.Test;

import java.util.stream.IntStream;

public class UsageExample {
    @Test
    public void test() throws Exception {
        IRateLimiter ratelimiter = new RateLimiter();
        IntStream.range(1, 10).parallel().forEach((i) -> {
            boolean passed = false;
            try {
                passed = ratelimiter.limit("app-1", "http://v1/user/12345");
            } catch (OverloadException e) {
                e.printStackTrace();
            } catch (InvalidUrlException e) {
                e.printStackTrace();
            } catch (InternalErrorException e) {
                e.printStackTrace();
            }
            System.out.println(passed ? "passed, " + i : "blocked, " + i);
        });
    }
}
