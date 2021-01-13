package entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Mapping to rate limit config
 */
@Setter
@Getter
public class ApiLimit {
    private static final int DEFAULT_TIME_UNIT = 1; // 1 second
    private String api;
    private int limit;
    private int unit;

    public ApiLimit() {}

    public ApiLimit(String api, int limit) {
        this(api, limit, DEFAULT_TIME_UNIT);
    }

    public ApiLimit(String api, int limit, int unit) {
        this.api = api;
        this.limit = limit;
        this.unit = unit;
    }
}
