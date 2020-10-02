package dasniko.keycloak.provider.email.aws;

import org.keycloak.Config;
import org.keycloak.email.EmailSenderProvider;
import org.keycloak.email.EmailSenderProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Niko Köbler, https://www.n-k.de, @dasniko
 */
public class AwsSesEmailSenderProviderFactory implements EmailSenderProviderFactory, ServerInfoAwareProviderFactory {

    private Map<String, String> configMap;
    private SesClient ses;

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

        Region region = Region.of(config.get("region"));
        this.ses = SesClient.builder().region(region).build();
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
        Map<String, String> info = new HashMap<>(configMap);
        info.put("AWS-API", "v2");
        return info;
    }
}
