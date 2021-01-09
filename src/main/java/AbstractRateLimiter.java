import entity.RuleConfig;
import exception.InternalErrorException;
import exception.InvalidUrlException;
import exception.UnsupportedMethodException;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;

/**
 * Abstract class for rate limiter.
 */
@Slf4j
public class AbstractRateLimiter implements IRateLimiter {
    private static final String RATELIMITER_RULE_YAML = "/ratelimiter-rule.yaml";

    protected RateLimitRule rateLimitRule;

    /**
     * Default construct.
     */
    public AbstractRateLimiter() {
        this(RATELIMITER_RULE_YAML);
    }

    public AbstractRateLimiter(String configFilePath) {
        InputStream in = null;
        RuleConfig ruleConfig = null;
        try {
            in = this.getClass().getResourceAsStream(configFilePath);
            if (in != null) {
                Yaml yaml = new Yaml();
                ruleConfig = yaml.loadAs(in, RuleConfig.class);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("File close error:{}", e);
                }
            }
        }
        this.rateLimitRule = new RateLimitRule(ruleConfig);
    }

    @Override
    public boolean limit(String appId, String url) throws InternalErrorException, InvalidUrlException {
        throw new UnsupportedMethodException("Unsupported method limit()");
    }
}
