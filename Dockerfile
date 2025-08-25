# maven 3 as 3.... version maven is used in this project
FROM maven:3-eclipse-temurin as build
#Copies all files from your project directory on your machine into the container’s working directory
COPY . .
#Executes Maven inside the container.
 #clean = deletes old build files.
 #package = builds your Spring Boot application into a JAR file.
 #-DskipTests = skips running tests (saves time in Docker builds).
 #After this, your .jar file is created inside /target/ folder in the container.
RUN mvn clean package -DskipTests
#21 as jdk 21 is used in this project, lighter than maven so container will be lighter
FROM eclipse-temurin:21-alpine
COPY --from=build /target/*.jar test.jar
#same port as application.properties
EXPOSE 9090
ENTRYPOINT ["java","-jar","test.jar"]
