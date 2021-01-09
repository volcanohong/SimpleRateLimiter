import entity.ApiLimit;
import entity.RuleConfig;
import exception.InvalidUrlException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Slf4j
public class RateLimitRule {

    /**
     * store <appId, limit rules> pairs.
     */
    private volatile ConcurrentMap<String, List<ApiLimit>> limitRules;

    public RateLimitRule(RuleConfig ruleConfig) {
        limitRules = ruleConfig.getConfigs()
                .stream()
                .collect(Collectors.toConcurrentMap(RuleConfig.AppRuleConfig::getAppId, RuleConfig.AppRuleConfig::getLimits));
    }

    public ApiLimit getLimit(String appId, String urlPath) throws InvalidUrlException {
        if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(urlPath)) {
            return null;
        }

        List<ApiLimit> limits = limitRules.get(appId);
        if (limits.isEmpty()) {
            return null;
        }

        ApiLimit apiLimit = getLimitInfo(limits, urlPath);
        return apiLimit;

    }

    /**
     * Get limit info for the url path.
     *
     * @param urlPath urlPath path
     * @return the limit info for the urlPath.
     * @throws InvalidUrlException if the url path is invalid.
     */
    public ApiLimit getLimitInfo(List<ApiLimit> limits, String urlPath) throws InvalidUrlException {
        if (StringUtils.isBlank(urlPath)) {
            return null;
        }

        for (ApiLimit limit : limits) {
            if (urlPath.contains(limit.getApi())) {
                log.info("url [{}] get limit info: {}.", urlPath, limit);
                return limit;
            }
        }

        throw new InvalidUrlException("UrlPath is not supported, url=" + urlPath);
    }
}
