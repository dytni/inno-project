FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

COPY pom.xml .

COPY auth/pom.xml auth/
COPY user-service/pom.xml user-service/

RUN mvn dependency:go-offline -B

COPY user-service/src user-service/src

RUN mvn clean package -pl user-service -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app


ARG JAR_FILE=/app/user-service/target/*.jar

COPY --from=builder ${JAR_FILE} app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["java", "-jar", "app.jar"]