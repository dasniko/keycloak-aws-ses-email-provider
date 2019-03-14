package dasniko.keycloak.provider.email.aws;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.keycloak.Config;
import org.keycloak.email.EmailSenderProvider;
import org.keycloak.email.EmailSenderProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

/**
 * @author Niko Köbler, https://www.n-k.de, @dasniko
 */
public class AwsSesEmailSenderProviderFactory implements EmailSenderProviderFactory {

    private AmazonSimpleEmailService ses;

    @Override
    public EmailSenderProvider create(KeycloakSession session) {
        return new AwsSesEmailSenderProvider(ses);
    }

    @Override
    public void init(Config.Scope config) {
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
}
