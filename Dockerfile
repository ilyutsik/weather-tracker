FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY build/libs/weather-tracker-*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
