package io.github.mvanbrummen.emailservice.service;

import io.github.mvanbrummen.emailservice.api.EmailSendRequest;
import io.github.mvanbrummen.emailservice.exception.EmailGatewayDownException;
import io.github.mvanbrummen.emailservice.gateway.EmailGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final EmailGateway sendgridEmailGateway;
    private final EmailGateway mailgunEmailGateway;

    @Retryable(maxAttempts = 1)
    public void sendEmail(final EmailSendRequest emailSendRequest) {
        mailgunEmailGateway.sendEmail(emailSendRequest);
    }

    @Recover
    public void sendEmailFallback(final Exception e, final EmailSendRequest emailSendRequest) {
        log.debug("Falling back to secondary email provider", e);
        try {
            sendgridEmailGateway.sendEmail(emailSendRequest);
        } catch (final RestClientException ex) {
            log.error("Failed to send to email provider", ex);
            throw new EmailGatewayDownException(ex);
        }
    }
}
