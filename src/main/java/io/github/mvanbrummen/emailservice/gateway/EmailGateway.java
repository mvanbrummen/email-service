package io.github.mvanbrummen.emailservice.gateway;

import io.github.mvanbrummen.emailservice.api.EmailSendRequest;

public interface EmailGateway {

    void sendEmail(EmailSendRequest emailSendRequest);
}
