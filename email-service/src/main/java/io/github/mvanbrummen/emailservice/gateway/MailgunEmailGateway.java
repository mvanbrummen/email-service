package io.github.mvanbrummen.emailservice.gateway;

import io.github.mvanbrummen.emailservice.api.EmailSendRequest;
import io.github.mvanbrummen.emailservice.api.Person;
import io.github.mvanbrummen.emailservice.exception.EmailGatewayDownException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class MailgunEmailGateway implements EmailGateway {
    private final RestTemplate mailgunRestTemplate;
    private final EmailGateway nextGateway;

    @Override
    public void sendEmail(final EmailSendRequest emailSendRequest) {
        try {
            final var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            final var form = buildForm(emailSendRequest);

            final var entity = new HttpEntity<>(form, headers);

            mailgunRestTemplate.exchange("/messages", HttpMethod.POST, entity, Void.class);
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

    static LinkedMultiValueMap<String, String> buildForm(final EmailSendRequest emailSendRequest) {
        final var form = new LinkedMultiValueMap<String, String>();
        form.put("to", emailSendRequest.getTo().stream()
                .map(Person::getEmail)
                .collect(Collectors.toList())
        );
        form.put("from", List.of(emailSendRequest.getFrom().getEmail()));
        form.put("subject", List.of(emailSendRequest.getSubject()));
        form.put("text", List.of(emailSendRequest.getContent()));

        form.put("cc", emailSendRequest.getCc().stream()
                .map(Person::getEmail)
                .collect(Collectors.toList())
        );

        form.put("bcc", emailSendRequest.getBcc().stream()
                .map(Person::getEmail)
                .collect(Collectors.toList())
        );
        return form;
    }
}
