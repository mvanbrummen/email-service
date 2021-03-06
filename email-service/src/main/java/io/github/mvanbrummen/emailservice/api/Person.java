package io.github.mvanbrummen.emailservice.api;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Person {
    @NotNull
    @Email
    private final String email;
    private final String name;
}
