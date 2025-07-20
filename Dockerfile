# maven 3 as 3.... version maven is used in this project
FROM maven:3-eclipse-temurin as build
COPY . .
RUN mvn clean package -DskipTests
#21 as jdk 21 is used in this project
FROM eclipse-temurin:21-alpine
COPY --from=build /target/*.jar test.jar
#same port as application.properties
EXPOSE 9090
ENTRYPOINT ["java","-jar","test.jar"]
