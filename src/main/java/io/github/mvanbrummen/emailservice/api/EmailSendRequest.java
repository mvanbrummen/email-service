package io.github.mvanbrummen.emailservice.api;

import lombok.Data;

import java.util.List;

@Data
public class EmailSendRequest {
    private final Person from;
    private final String subject;
    private final String content;
    private final List<Person> to;
    private final List<Person> cc;
    private final List<Person> bcc;
}
