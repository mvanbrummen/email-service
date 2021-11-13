package io.github.mvanbrummen.emailservice.exception;

public class EmailGatewayDownException extends RuntimeException {

    public EmailGatewayDownException() {
    }

    public EmailGatewayDownException(final Throwable cause) {
        super(cause);
    }
}
