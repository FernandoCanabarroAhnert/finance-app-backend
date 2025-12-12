FROM openjdk:21-jdk
WORKDIR /app
COPY target/booking-app-backend-0.0.1-SNAPSHOT.jar /app/booking-app-backend.jar
EXPOSE 8080
CMD ["java", "-jar", "booking-app-backend.jar"]