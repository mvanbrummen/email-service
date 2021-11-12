package io.github.mvanbrummen.emailservice.service;

import io.github.mvanbrummen.emailservice.api.EmailSendRequest;
import io.github.mvanbrummen.emailservice.gateway.EmailGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailGateway sendgridEmailGateway;
    private final EmailGateway mailgunEmailGateway;

    public void sendEmail(final EmailSendRequest emailSendRequest) {
        mailgunEmailGateway.sendEmail(emailSendRequest);
    }

}
