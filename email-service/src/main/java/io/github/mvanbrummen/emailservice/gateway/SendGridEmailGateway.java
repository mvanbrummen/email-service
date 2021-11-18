package io.github.mvanbrummen.emailservice.gateway;

import io.github.mvanbrummen.emailservice.api.EmailSendRequest;
import io.github.mvanbrummen.emailservice.api.gateway.SendGridEmailRequest;
import io.github.mvanbrummen.emailservice.exception.EmailGatewayDownException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
public class SendGridEmailGateway implements EmailGateway {
    private final RestTemplate sendgridRestTemplate;
    private final EmailGateway nextGateway;

    @Override
    public void sendEmail(final EmailSendRequest emailSendRequest) {
        try {
            final var request = SendGridEmailRequest.fromEmailSendRequest(emailSendRequest);

            final var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            final var entity = new HttpEntity<>(request, headers);

            sendgridRestTemplate.exchange("/mail/send", HttpMethod.POST, entity, Void.class);
        } catch (Exception e) {
            log.error("Failed to send email", e);

           if (getNext() != null) {
                getNext().sendEmail(emailSendRequest);
            } else {
                throw new EmailGatewayDownException(e);
            }
        }
    }

    @Override
    public EmailGateway getNext() {
        return nextGateway;
    }
}
