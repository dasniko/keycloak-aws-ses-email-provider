package dasniko.keycloak.provider.email.aws;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import org.keycloak.email.EmailException;
import org.keycloak.email.EmailSenderProvider;
import org.keycloak.services.ServicesLogger;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;

/**
 * @author Niko Köbler, https://www.n-k.de, @dasniko
 */
public class AwsSesEmailSenderProvider implements EmailSenderProvider {

    private static final String UTF8 = "utf-8";

    private final AmazonSimpleEmailService ses;

    AwsSesEmailSenderProvider(AmazonSimpleEmailService ses) {
        this.ses = ses;
    }

    public void send(Map<String, String> config, org.keycloak.models.UserModel user, String subject, String textBody, String htmlBody) throws EmailException {
        String configurationset = config.get("configurationset");

        String from = config.get("from");
        String fromDisplayName = config.get("fromDisplayName");
        String replyTo = config.get("replyTo");
        String replyToDisplayName = config.get("replyToDisplayName");

        try {
            SendEmailRequest sendEmailRequest = new SendEmailRequest()
                .withDestination(
                    new Destination().withToAddresses(user.getEmail())
                )
                .withMessage(new Message()
                    .withSubject(new Content().withCharset(UTF8).withData(subject))
                    .withBody(new Body()
                        .withHtml(new Content().withCharset(UTF8).withData(htmlBody))
                        .withText(new Content().withCharset(UTF8).withData(textBody))
                    )
                )
                .withSource(toInternetAddress(from, fromDisplayName).toString());

            if (replyTo != null && !replyTo.isEmpty()) {
                sendEmailRequest.setReplyToAddresses(
                    Collections.singletonList(toInternetAddress(replyTo, replyToDisplayName).toString()));
            }

            if (configurationset != null && !configurationset.isEmpty()) {
                sendEmailRequest.setConfigurationSetName(configurationset);
            }

            ses.sendEmail(sendEmailRequest);

        } catch (Exception e) {
            ServicesLogger.LOGGER.failedToSendEmail(e);
            throw new EmailException(e);
        }
    }

    private InternetAddress toInternetAddress(String email, String displayName) throws UnsupportedEncodingException, AddressException, EmailException {
        if (email == null || "".equals(email.trim())) {
            throw new EmailException("Please provide a valid address", null);
        }
        if (displayName == null || "".equals(displayName.trim())) {
            return new InternetAddress(email);
        }
        return new InternetAddress(email, displayName, UTF8);
    }

    @Override
    public void close() {
    }
}
