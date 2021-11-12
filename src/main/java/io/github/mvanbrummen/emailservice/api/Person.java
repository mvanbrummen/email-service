package io.github.mvanbrummen.emailservice.api;

import lombok.Data;

@Data
public class Person {
    private final String email;
    private final String name;
}
