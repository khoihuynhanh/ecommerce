FROM maven:3.9.0-eclipse-temurin-17-alpine as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM adoptopenjdk/openjdk11:jdk-11.0.19_7-slim
WORKDIR /app
COPY --from=build /app/target/ecommerce-0.0.1-SNAPSHOT.jar ecommerce-0.0.1-SNAPSHOT.jar

EXPOSE 8080
ENTRYPOINT ["java", "-cp", "ecommerce-0.0.1-SNAPSHOT.jar:ojdbc8.jar"]
