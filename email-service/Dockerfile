FROM maven:3.8.3-openjdk-17 as compile
COPY ../src ../pom.xml /build
WORKDIR /build
# Add the hoverfly cert to remove TLS errors. Should really configure the SSL context in the rest template for the intg test (https://docs.hoverfly.io/projects/hoverfly-java/en/latest/pages/misc/misc.html#trusting-hoverfly-certificate)
RUN $JAVA_HOME/bin/keytool -import -alias hoverfly -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt -file cert.pem
RUN --mount=type=cache,target=/root/.m2 mvn clean install

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=compile "/build/target/email-service-0.0.1-SNAPSHOT.jar" .
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app/email-service-0.0.1-SNAPSHOT.jar"]