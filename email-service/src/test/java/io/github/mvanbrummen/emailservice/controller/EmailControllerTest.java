package io.github.mvanbrummen.emailservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mvanbrummen.emailservice.TestData;
import io.github.mvanbrummen.emailservice.api.EmailSendRequest;
import io.github.mvanbrummen.emailservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EmailController.class)
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmailService emailService;

    @Test
    void shouldReturn202AcceptedWhenValidRequest() throws Exception {
        var request = TestData.emailSendRequest();

        mockMvc.perform(post("/email/send")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        verify(emailService).sendEmail(request);
    }

    @Test
    void shouldReturn400WhenMandatoryFieldsAreMissing() throws Exception {
        var request = EmailSendRequest.builder().build();

        mockMvc.perform(post("/email/send")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                          "subject": "must not be null",
                          "from": "must not be null",
                          "to": "must not be empty",
                          "content": "must not be null"
                        }
                        """));
    }

    @Test
    void shouldReturn400WhenEmailValidationFails() throws Exception {
        var request = TestData.emailSendRequestInvalidEmails();

        mockMvc.perform(post("/email/send")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                          "to[0].email": "must be a well-formed email address",
                          "bcc[0].email": "must be a well-formed email address",
                          "cc[0].email": "must be a well-formed email address",
                          "from.email": "must be a well-formed email address"
                        }
                        """));
    }
}