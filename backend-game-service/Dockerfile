# Gebruik de OpenJDK 17-slim image als basis
FROM openjdk:17-slim

WORKDIR /app

# Kopieer alleen de vereiste bestanden
COPY app.jar /app/app.jar
COPY src/main/resources/googleCloud /app/resources/googleCloud

ENV GOOGLE_APPLICATION_CREDENTIALS=/app/resources/googleCloud/ip2-team6-442207-7969adb3b63a.json

# Exposeer de poort
EXPOSE 8081

# Start de applicatie
CMD ["java", "-jar", "/app/app.jar"]