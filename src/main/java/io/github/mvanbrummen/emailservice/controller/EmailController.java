package io.github.mvanbrummen.emailservice.controller;

import io.github.mvanbrummen.emailservice.api.EmailSendRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class EmailController {

    @PostMapping("/email/send")
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody final EmailSendRequest emailSendRequest) {
        // send email
        return ResponseEntity.accepted().build();
    }

}
