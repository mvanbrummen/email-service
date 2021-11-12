package io.github.mvanbrummen.emailservice.api;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Person {
    @NotNull
    private final String email;
    private final String name;
}
