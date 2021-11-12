package io.github.mvanbrummen.emailservice.controller;

import io.github.mvanbrummen.emailservice.api.EmailSendRequest;
import io.github.mvanbrummen.emailservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/email/send")
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody final EmailSendRequest emailSendRequest) {
        emailService.sendEmail(emailSendRequest);
        return ResponseEntity.accepted().build();
    }

}
