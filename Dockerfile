# syntax=docker/dockerfile:1
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw -B dependency:go-offline

COPY src src
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:21-jre
RUN groupadd --system prices && useradd --system --gid prices --home-dir /app prices
WORKDIR /app
COPY --from=build /workspace/target/prices-*.jar app.jar
RUN chown -R prices:prices /app
USER prices

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
