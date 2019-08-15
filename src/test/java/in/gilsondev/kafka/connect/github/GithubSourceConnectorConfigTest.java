package in.gilsondev.kafka.connect.github;

import org.apache.commons.collections.map.HashedMap;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class GithubSourceConnectorConfigTest {
    private GithubSourceConnectorConfig githubConfig;
    private Map<String, String> initialValue;
    private ConfigDef configDef = githubConfig.config();

    private Map<String, String> initialConfig() {
        Map<String, String> baseProperties = new HashedMap();
        baseProperties.put(githubConfig.getOwnerConfig(), "foo");
        baseProperties.put(githubConfig.getRepoConfig(), "bar");
        baseProperties.put(githubConfig.getSinceConfig(), "2019-04-26T01:23:45Z");
        baseProperties.put(githubConfig.getBatchSizeConfig(), "100");
        baseProperties.put(githubConfig.getTopicConfig(), "github-issues");

        return baseProperties;
    }

    @BeforeEach
    public void setUp() {
        this.initialValue = initialConfig();
        this.githubConfig = new GithubSourceConnectorConfig(initialConfig());
    }

    @Test
    public void initialConfigIsValid() {
        assert (configDef.validate(initialConfig()))
                .stream()
                .allMatch(configValue -> configValue.errorMessages().size() == 0);
    }

    @Test
    public void canReadConfigCorrectly() {
        initialValue.put(githubConfig.getAuthUsernameConfig(), "username@mail.com");
        initialValue.put(githubConfig.getAuthPasswordConfig(), "password123");
        githubConfig = new GithubSourceConnectorConfig(initialValue);

        assert githubConfig.getString(githubConfig.getOwnerConfig()) == "foo";
        assert githubConfig.getString(githubConfig.getRepoConfig()) == "bar";
        assert githubConfig.getString(githubConfig.getSinceConfig()) == "2019-04-26T01:23:45Z";
        assert githubConfig.getInt(githubConfig.getBatchSizeConfig()) == 100;
        assert githubConfig.getString(githubConfig.getTopicConfig()) == "github-issues";
        assert githubConfig.getString(githubConfig.getAuthUsernameConfig()) == "username@mail.com";
        assert githubConfig.getPassword(githubConfig.getAuthPasswordConfig()).value() == "password123";
    }

    @Test
    public void testInvalidSinceProperty() {
        initialValue.put(githubConfig.getSinceConfig(), "not-a-date");

        ConfigValue configValue = configDef.validateAll(initialValue).get(githubConfig.getSinceConfig());
        assert configValue.errorMessages().size() > 0;
    }

    @Test
    public void validateBatchSize() {
        this.initialValue.put(githubConfig.getBatchSizeConfig(), "-1");
        ConfigValue configValue = configDef.validateAll(this.initialValue).get(githubConfig.getBatchSizeConfig());
        assert configValue.errorMessages().size() > 0;

        this.initialValue.put(githubConfig.getBatchSizeConfig(), "101");
        configValue = configDef.validateAll(this.initialValue).get(githubConfig.getBatchSizeConfig());
        assert configValue.errorMessages().size() > 0;
    }

    @Test
    public void validateUsernameProperty() {
        this.initialValue.put(githubConfig.getAuthUsernameConfig(), "username@mail.com");
        ConfigValue configValue = configDef.validateAll(initialValue).get(githubConfig.getAuthUsernameConfig());
        assert configValue.errorMessages().size() == 0;
    }

    @Test
    public void validatePasswordProperty() {
        this.initialValue.put(githubConfig.getAuthPasswordConfig(), "password123");
        ConfigValue configValue = configDef.validateAll(initialValue).get(githubConfig.getAuthPasswordConfig());
        assert configValue.errorMessages().size() == 0;
    }
}
