package in.gilsondev.kafka.connect.github;

import in.gilsondev.kafka.connect.github.validators.BatchSizeValidator;
import in.gilsondev.kafka.connect.github.validators.TimestampValidator;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.time.ZonedDateTime;
import java.util.Map;

public class GithubSourceConnectorConfig extends AbstractConfig {
    public static final String TOPIC_CONFIG = "topic";
    public static final String TOPIC_DOC = "Topic to write to";

    public static final String OWNER_CONFIG = "github.owner";
    public static final String OWNER_DOC = "Owner of the repository you'd like to follow";

    public static final String REPO_CONFIG = "github.repo";
    public static final String REPO_DOC = "Repository you'd like to follow";

    public static final String SINCE_CONFIG = "since.timestamp";
    public static final String SINCE_DOC = "Only issues updated at or other this time are returned.\n" +
            "This is a timestamp in ISO 8601 format: YYYY-MM-DDTHH:MM:SSZ.\n" +
            "Default to a year from first launch.";

    public static final String BATCH_SIZE_CONFIG = "batch.size";
    public static final String BATCH_SIZE_DOC = "Number of data points to retrieve at a time.\n" +
            "Defaults to 100 (max value)";

    public static final String AUTH_USERNAME_CONFIG = "auth.username";
    public static final String AUTH_USERNAME_DOC = "Optional username to authenticate calls";

    public static final String AUTH_PASSWORD_CONFIG = "auth.password";
    public static final String AUTH_PASSWORD_DOC = "Optional password to authenticate calls";

    public GithubSourceConnectorConfig(ConfigDef definition, Map<String, String> originals) {
        super(definition, originals);
    }

    public GithubSourceConnectorConfig(Map<String, String> originals) {
        this(config(), originals);
    }

    public static ConfigDef config() {
        return new ConfigDef()
            .define(TOPIC_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, TOPIC_DOC)
            .define(OWNER_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, OWNER_DOC)
            .define(REPO_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, REPO_DOC)
            .define(BATCH_SIZE_CONFIG, ConfigDef.Type.INT, 100,
                    new BatchSizeValidator(), ConfigDef.Importance.LOW, BATCH_SIZE_DOC)
            .define(SINCE_CONFIG, ConfigDef.Type.STRING, ZonedDateTime.now().minusYears(1).toInstant().toString(),
                    new TimestampValidator(), ConfigDef.Importance.HIGH, SINCE_DOC)
            .define(AUTH_USERNAME_CONFIG, ConfigDef.Type.STRING, "", ConfigDef.Importance.HIGH, AUTH_PASSWORD_DOC)
            .define(AUTH_PASSWORD_CONFIG, ConfigDef.Type.PASSWORD, "", ConfigDef.Importance.HIGH, AUTH_PASSWORD_DOC);
    }

    public static String getTopicConfig() {
        return TOPIC_CONFIG;
    }

    public static String getOwnerConfig() {
        return OWNER_CONFIG;
    }

    public static String getRepoConfig() {
        return REPO_CONFIG;
    }

    public static String getSinceConfig() {
        return SINCE_CONFIG;
    }

    public static String getBatchSizeConfig() {
        return BATCH_SIZE_CONFIG;
    }

    public static String getAuthUsernameConfig() {
        return AUTH_USERNAME_CONFIG;
    }

    public static String getAuthPasswordConfig() {
        return AUTH_PASSWORD_CONFIG;
    }
}
