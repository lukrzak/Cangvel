FROM eclipse-temurin:17-jdk-alpine
LABEL authors="lukrzak"

USER root
ADD target/Cangvel-0.0.1-SNAPSHOT.jar Cangvel.jar

ENTRYPOINT ["java", "-jar", "Cangvel.jar"]
