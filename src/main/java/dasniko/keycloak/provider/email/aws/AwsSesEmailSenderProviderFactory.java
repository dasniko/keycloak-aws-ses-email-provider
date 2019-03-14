package dasniko.keycloak.provider.email.aws;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.keycloak.Config;
import org.keycloak.email.EmailSenderProvider;
import org.keycloak.email.EmailSenderProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Niko Köbler, https://www.n-k.de, @dasniko
 */
public class AwsSesEmailSenderProviderFactory implements EmailSenderProviderFactory, ServerInfoAwareProviderFactory {

    private Map<String, String> configMap;
    private AmazonSimpleEmailService ses;

    @Override
    public EmailSenderProvider create(KeycloakSession session) {
        return new AwsSesEmailSenderProvider(configMap, ses);
    }

    @Override
    public void init(Config.Scope config) {
        String[] configKeys = {"region", "configSetName", "from", "fromDisplayName", "replyTo", "replyToDisplayName"};
        this.configMap = new HashMap<>();
        for (String key : configKeys) {
            configMap.put(key, config.get(key));
        }

        String region = config.get("region", Regions.EU_WEST_1.getName());
        this.ses = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(region).build();
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return "aws-ses";
    }

    @Override
    public Map<String, String> getOperationalInfo() {
        return configMap;
    }
}
