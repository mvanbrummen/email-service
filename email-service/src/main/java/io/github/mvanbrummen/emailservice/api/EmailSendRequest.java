package io.github.mvanbrummen.emailservice.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EmailSendRequest {
    @NotNull
    @Valid
    private final Person from;
    @NotNull
    private final String subject;
    @NotNull
    private final String content;

    @NotEmpty
    @Valid
    private final List<Person> to;
    @Valid
    @Builder.Default
    private final List<Person> cc = List.of();
    @Valid
    @Builder.Default
    private final List<Person> bcc = List.of();
}
