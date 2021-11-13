package io.github.mvanbrummen.emailservice;

import io.github.mvanbrummen.emailservice.api.EmailSendRequest;
import io.github.mvanbrummen.emailservice.api.Person;

import java.util.List;

public class TestData {

    public static EmailSendRequest emailSendRequest() {
        return EmailSendRequest.builder()
                .to(List.of(Person.builder().email("michaelvanbrummen@gmail.com").build()))
                .from(Person.builder().email("michaelvanbrummen@icloud.com").build())
                .content("Test Content")
                .subject("Test Subject")
                .build();
    }

    public static EmailSendRequest emailSendRequestInvalidEmails() {
        return EmailSendRequest.builder()
                .to(List.of(Person.builder().email("plainaddress").build()))
                .from(Person.builder().email("#@%^%#$@#$@#.com").build())
                .cc(List.of(Person.builder().email("@example.com").build()))
                .bcc(List.of(Person.builder().email("email@example.com (Joe Smith)").build()))
                .content("Test Content")
                .subject("Test Subject")
                .build();
    }
}
