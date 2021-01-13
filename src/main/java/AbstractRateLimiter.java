import entity.RuleConfig;
import exception.InternalErrorException;
import exception.InvalidUrlException;
import exception.UnsupportedMethodException;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;

/**
 * Rate limiter template.
 *
 * TODO extend to read from JSON as well
 */
@Slf4j
public abstract class AbstractRateLimiter implements IRateLimiter {

    private static final String RATE_LIMIT_RULE_YAML = "/rate-limit-rule.yaml";

    protected RateLimitRule rateLimitRule;

    /**
     * Default construct.
     */
    public AbstractRateLimiter() {
        this(RATE_LIMIT_RULE_YAML);
    }

    /**
     * Construct
     *
     * TODO extract the yaml parsing to Utils
     * TODO using strategy pattern for diff. parsing logic
     *
     * @param configFilePath the path of rate limit config file
     */
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
