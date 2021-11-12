package io.github.mvanbrummen.emailservice.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    @Qualifier("sendgridRestTemplate")
    public RestTemplate sendgridRestTemplate(@Value("${email.gateway.sendgrid.base-url}") final String baseUrl,
                                             @Value("${email.gateway.sendgrid.api-key}") final String apiKey) {
        return new RestTemplateBuilder()
                .rootUri(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    @Bean
    @Qualifier("mailgunRestTemplate")
    public RestTemplate mailgunRestTemplate(@Value("${email.gateway.mailgun.base-url}") final String baseUrl,
                                            @Value("${email.gateway.mailgun.api-key}") final String apiKey) {
        return new RestTemplateBuilder()
                .rootUri(baseUrl)
                .basicAuthentication("api", apiKey)
                .build();
    }
}
