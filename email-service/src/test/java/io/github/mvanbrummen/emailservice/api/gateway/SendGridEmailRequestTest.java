package io.github.mvanbrummen.emailservice.api.gateway;

import io.github.mvanbrummen.emailservice.TestData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SendGridEmailRequestTest {

    @Test
    void fromEmailSendRequest_shouldReturnSendGridEmailRequestWhenValidEmailRequest() {
        var request = TestData.emailSendRequest();
        var result = SendGridEmailRequest.fromEmailSendRequest(request);

        assertThat(result).isNotNull().hasFieldOrPropertyWithValue("from.email", "michaelvanbrummen@icloud.com")
                .hasFieldOrPropertyWithValue("subject", "Test Subject");
        assertThat(result.getPersonalizations().get(0).getTo().get(0)).hasFieldOrPropertyWithValue("email", "michaelvanbrummen@gmail.com");
        assertThat(result.getContent().get(0)).hasFieldOrPropertyWithValue("type", "text/plain")
                .hasFieldOrPropertyWithValue("value", "Test Content");
    }
}