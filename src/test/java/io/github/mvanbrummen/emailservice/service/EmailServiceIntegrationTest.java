package io.github.mvanbrummen.emailservice.service;

import io.github.mvanbrummen.emailservice.TestData;
import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit5.HoverflyExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.serverError;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.matches;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(HoverflyExtension.class)
class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @Test
    void shouldSendEmailToPrimaryProviderWhenRequestIsSuccess(Hoverfly hoverfly) throws Exception {
        var request = TestData.emailSendRequest();

        hoverfly.simulate(dsl(
                service("api.mailgun.net")
                        .post("/v3/sandbox3793fb4e82f6408281f7901e8578d9fc.mailgun.org/messages")
                        .body("to=michaelvanbrummen%40gmail.com&from=michaelvanbrummen%40icloud.com&subject=Test+Subject&text=Test+Content")
                        .willReturn(success("""
                                {
                                  "id": "<20211112053919.32086b1dc8d5e76e@sandbox3793fb4e82f6408281f7901e8578d9fc.mailgun.org>",
                                  "message": "Queued. Thank you."
                                }
                                """, MediaType.APPLICATION_JSON_VALUE))
        ));

        emailService.sendEmail(request);

        hoverfly.verifyZeroRequestTo(service(matches("api.sendgrid.com")));
    }

    @Test
    void shouldSendEmailToFallbackProviderWhenRequestFails(Hoverfly hoverfly) throws Exception {
        var request = TestData.emailSendRequest();

        hoverfly.simulate(dsl(
                service("api.mailgun.net")
                        .post("/v3/sandbox3793fb4e82f6408281f7901e8578d9fc.mailgun.org/messages")
                        .body("to=michaelvanbrummen%40gmail.com&from=michaelvanbrummen%40icloud.com&subject=Test+Subject&text=Test+Content")
                        .willReturn(serverError())
        ));

        hoverfly.simulate(dsl(
                service("api.sendgrid.com")
                        .post("/v3/mail/send")
                        .body("""
                                {
                                  "from": {
                                    "email": "michaelvanbrummen@icloud.com",
                                    "name": null
                                  },
                                  "personalizations": [
                                    {
                                      "to": [
                                        {
                                          "email": "michaelvanbrummen@gmail.com",
                                          "name": null
                                        }
                                      ],
                                      "cc": null,
                                      "bcc": null
                                    }
                                  ],
                                  "subject": "Test Subject",
                                  "content": [
                                    {
                                      "type": "text/plain",
                                      "value": "Test Content"
                                    }
                                  ]
                                }
                                    """
                        )
                        .willReturn(success())
        ));

        emailService.sendEmail(request);
    }
}
