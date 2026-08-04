FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

COPY pom.xml .

COPY auth/pom.xml auth/
COPY user-service/pom.xml user-service/
COPY common/pom.xml common/
COPY common/common-events/pom.xml common/common-events/
COPY common/common-hibernate/pom.xml common/common-hibernate/
COPY common/common-security/pom.xml common/common-security/
COPY common/common-redis/pom.xml common/common-redis/

RUN mvn dependency:go-offline -B


COPY common/common-events/ common/common-events/
COPY common/common-hibernate/ common/common-hibernate/
COPY common/common-security/ common/common-security/
COPY common/common-redis/ common/common-redis/
COPY auth/src auth/src

RUN mvn clean package -pl auth -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app


ARG JAR_FILE=/app/auth/target/*.jar

COPY --from=builder ${JAR_FILE} app.jar

EXPOSE 8081

ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["java", "-jar", "app.jar"]