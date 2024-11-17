FROM eclipse-temurin:17-jdk-noble AS build

WORKDIR /app

COPY . ./

ENV GRADLE_OPTS -Dkotlin.compiler.execution.strategy="in-process"

RUN ./gradlew assembleShadowDist --no-daemon

RUN tar -xf build/distributions/backend-shadow-0.0.1.tar

FROM eclipse-temurin:17-jre-noble

COPY --from=build /app/backend-shadow-0.0.1 /app

WORKDIR /app

EXPOSE 8000:8000

ENTRYPOINT ["/bin/sh", "/app/bin/backend"]
