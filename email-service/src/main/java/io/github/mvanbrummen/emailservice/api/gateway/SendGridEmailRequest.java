package io.github.mvanbrummen.emailservice.api.gateway;

import io.github.mvanbrummen.emailservice.api.EmailSendRequest;
import io.github.mvanbrummen.emailservice.api.Person;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.MediaType;

import java.util.List;

@Data
@Builder
public class SendGridEmailRequest {
    private final Person from;
    private final List<Personalizations> personalizations;
    private final String subject;
    private final List<Content> content;

    @Data
    @Builder
    static class Personalizations {
        private final List<Person> to;
        private final List<Person> cc;
        private final List<Person> bcc;
    }

    @Data
    @Builder
    static class Content {
        private final String type;
        private final String value;
    }

    public static SendGridEmailRequest fromEmailSendRequest(final EmailSendRequest emailSendRequest) {
        return SendGridEmailRequest.builder()
                .from(emailSendRequest.getFrom())
                .personalizations(
                        List.of(
                                Personalizations.builder()
                                        .to(emailSendRequest.getTo())
                                        .cc(emailSendRequest.getCc())
                                        .bcc(emailSendRequest.getBcc())
                                        .build()
                        )
                )
                .subject(emailSendRequest.getSubject())
                .content(List.of(
                        Content.builder()
                                .type(MediaType.TEXT_PLAIN_VALUE)
                                .value(emailSendRequest.getContent())
                                .build()
                ))
                .build();
    }
}
