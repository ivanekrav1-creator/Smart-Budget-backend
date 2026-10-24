FROM gradle:9.4.0-jdk25 AS builder
WORKDIR /app
COPY . .

RUN ./gradlew :service:smart-budget:bootJar -x :integration-tests:test -x test
RUN ./gradlew :mock-producer:bootJar -x :integration-tests:test -x test

FROM eclipse-temurin:25-jre-jammy AS extractor-consumer
WORKDIR /app
COPY --from=builder /app/service/smart-budget/build/libs/*.jar app-consumer.jar
RUN java -Djarmode=layertools -jar app-consumer.jar extract

FROM eclipse-temurin:25-jre-jammy AS extractor-producer
WORKDIR /app
COPY --from=builder /app/mock-producer/build/libs/*.jar app-producer.jar
RUN java -Djarmode=layertools -jar app-producer.jar extract

FROM eclipse-temurin:25-jre-jammy AS consumer
WORKDIR /application
COPY --from=extractor-consumer /app/dependencies/ ./
COPY --from=extractor-consumer /app/spring-boot-loader/ ./
COPY --from=extractor-consumer /app/snapshot-dependencies/ ./
COPY --from=extractor-consumer /app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

FROM eclipse-temurin:25-jre-jammy AS producer
WORKDIR /application
COPY --from=extractor-producer /app/dependencies/ ./
COPY --from=extractor-producer /app/spring-boot-loader/ ./
COPY --from=extractor-producer /app/snapshot-dependencies/ ./
COPY --from=extractor-producer /app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]