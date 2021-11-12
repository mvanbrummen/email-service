package io.github.mvanbrummen.emailservice.service;

import io.github.mvanbrummen.emailservice.api.EmailSendRequest;
import io.github.mvanbrummen.emailservice.gateway.EmailGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailGateway sendgridEmailGateway;

    public void sendEmail(final EmailSendRequest emailSendRequest) {
        sendgridEmailGateway.sendEmail(emailSendRequest);
    }

}
