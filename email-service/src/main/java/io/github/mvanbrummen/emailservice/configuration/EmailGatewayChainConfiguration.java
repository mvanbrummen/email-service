package io.github.mvanbrummen.emailservice.configuration;

import io.github.mvanbrummen.emailservice.gateway.EmailGateway;
import io.github.mvanbrummen.emailservice.gateway.MailgunEmailGateway;
import io.github.mvanbrummen.emailservice.gateway.SendGridEmailGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class EmailGatewayChainConfiguration {
    private final RestTemplate sendgridRestTemplate;
    private final RestTemplate mailgunRestTemplate;

    @Bean
    public EmailGateway emailGateway() {
        var last = new SendGridEmailGateway(sendgridRestTemplate, null);
        var first = new MailgunEmailGateway(mailgunRestTemplate, last);

        return first;
    }
}
