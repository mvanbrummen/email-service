package io.github.mvanbrummen.emailservice.gateway;

import io.github.mvanbrummen.emailservice.api.EmailSendRequest;
import io.github.mvanbrummen.emailservice.api.gateway.SendGridEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("sendgridEmailGateway")
@RequiredArgsConstructor
public class SendGridEmailGateway implements EmailGateway {
    private final RestTemplate sendgridRestTemplate;

    @Override
    public void sendEmail(final EmailSendRequest emailSendRequest) {
        final var request = SendGridEmailRequest.fromEmailSendRequest(emailSendRequest);

        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final var entity = new HttpEntity<>(request, headers);

        sendgridRestTemplate.exchange("/mail/send", HttpMethod.POST, entity, Void.class);
    }
}
