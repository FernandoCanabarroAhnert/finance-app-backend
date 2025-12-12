FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/finance-app-backend-0.0.1-SNAPSHOT.jar /app/finance-app-backend.jar
EXPOSE 8080
CMD ["java", "-jar", "finance-app-backend.jar"]