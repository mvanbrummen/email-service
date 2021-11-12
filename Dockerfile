FROM maven:3.8.3-openjdk-17 as compile
COPY . /build
WORKDIR /build
RUN mvn clean install

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=compile "/build/target/email-service-0.0.1-SNAPSHOT.jar" .
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app/email-service-0.0.1-SNAPSHOT.jar"]