package io.github.mvanbrummen.emailservice.gateway;

import io.github.mvanbrummen.emailservice.api.EmailSendRequest;
import io.github.mvanbrummen.emailservice.api.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Component("mailgunEmailGateway")
@RequiredArgsConstructor
public class MailgunEmailGateway implements EmailGateway {
    private final RestTemplate mailgunRestTemplate;

    @Override
    public void sendEmail(final EmailSendRequest emailSendRequest) {
        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        final var form = buildForm(emailSendRequest);

        final var entity = new HttpEntity<>(form, headers);

        mailgunRestTemplate.exchange("/messages", HttpMethod.POST, entity, Void.class);
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

        if (emailSendRequest.getCc() != null) {
            form.put("cc", emailSendRequest.getCc().stream()
                    .map(Person::getEmail)
                    .collect(Collectors.toList())
            );
        }

        if (emailSendRequest.getBcc() != null) {
            form.put("bcc", emailSendRequest.getBcc().stream()
                    .map(Person::getEmail)
                    .collect(Collectors.toList())
            );
        }
        return form;
    }
}
