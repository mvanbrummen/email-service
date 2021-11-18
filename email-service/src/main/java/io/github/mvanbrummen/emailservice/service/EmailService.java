package io.github.mvanbrummen.emailservice.service;

import io.github.mvanbrummen.emailservice.api.EmailSendRequest;
import io.github.mvanbrummen.emailservice.gateway.EmailGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final EmailGateway emailGateway;

    public void sendEmail(final EmailSendRequest emailSendRequest) {
        emailGateway.sendEmail(emailSendRequest);
    }
}
