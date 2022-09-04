FROM eclipse-temurin:17-jdk-alpine as builder

COPY ./service ./

RUN ./mvnw package


FROM eclipse-temurin:17-jdk-alpine as runner
VOLUME /tmp

COPY --from=builder /target/*.jar ./service.jar

ENTRYPOINT ["java","-jar","service.jar"]
