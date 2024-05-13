FROM maven:3.8.3-openjdk-17 AS builder

WORKDIR /src/a13autehnticate
COPY . .
RUN ./mvnw clean package

FROM openjdk:17.0.1-jdk-slim AS runner

ARG USER_NAME=a13autehnticate
ARG USER_UID=1000
ARG USER_GID=${USER_UID}

RUN addgroup --gid ${USER_GID} ${USER_NAME} \
&& adduser --home /opt/a13autehnticate --disabled-password --uid ${USER_UID} --ingroup ${USER_NAME} ${USER_NAME}

USER ${USER_NAME}
WORKDIR /opt/a13autehnticate
COPY --from=builder --chown=${USER_UID}:${USER_GID} /src/a13autehnticate/target/*.jar app.jar

ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]