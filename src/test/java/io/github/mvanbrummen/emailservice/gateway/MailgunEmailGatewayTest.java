package io.github.mvanbrummen.emailservice.gateway;

import io.github.mvanbrummen.emailservice.TestData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MailgunEmailGatewayTest {

    @Test
    void shouldReturnFormWhenValidEmailSendRequest() {
        var request = TestData.emailSendRequest();

        var result = MailgunEmailGateway.buildForm(request);

        assertThat(result.get("subject")).containsOnly("Test Subject");
        assertThat(result.get("text")).containsOnly("Test Content");
        assertThat(result.get("to")).containsOnly("michaelvanbrummen@gmail.com");
        assertThat(result.get("from")).containsOnly("michaelvanbrummen@icloud.com");
    }
}