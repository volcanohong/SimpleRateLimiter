package entity;

import lombok.Data;

import java.util.List;

/**
 * Mapping to rate limit config
 */
@Data
public class RuleConfig {
    private List<AppRuleConfig> configs;

    @Data
    public static class AppRuleConfig {
        private String appId;
        private List<ApiLimit> limits;
    }
}
