package dasniko.keycloak.provider.email.aws;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.keycloak.email.EmailException;
import org.keycloak.models.UserModel;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Niko Köbler, https://www.n-k.de, @dasniko
 */
class AwsSesEmailSenderProviderIT {

    @Test
    @Disabled("Run only manually")
    void testSend() throws EmailException {
        AmazonSimpleEmailService ses = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.EU_WEST_1).build();

        Map<String, String> configMap = new HashMap<>();
        configMap.put("from", "niko@n-k.de");
        configMap.put("fromDisplayName", "Keycloak Test");

        UserModel user = mock(UserModel.class);
        when(user.getEmail()).thenReturn("niko@n-k.de");

        AwsSesEmailSenderProvider provider = new AwsSesEmailSenderProvider(configMap, ses);
        provider.send(null, user, "Test", "Hello", "<h1>Hello</h1>");
    }
}
