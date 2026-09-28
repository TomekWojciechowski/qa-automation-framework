FROM maven:3.9-eclipse-temurin-17

WORKDIR /app

# Resolve dependencies first so they are cached between builds
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src

# Browser and grid URL are injected at runtime (see docker-compose.yml)
ENTRYPOINT ["mvn", "-B", "test"]
