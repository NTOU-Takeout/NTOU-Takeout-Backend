FROM gradle:7.6.4-jdk-focal AS build
WORKDIR /app
COPY ./src ./src
COPY ./build.gradle ./settings.gradle ./
RUN gradle clean build -x test

FROM openjdk:17-oracle
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
