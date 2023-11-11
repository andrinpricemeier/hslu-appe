FROM openjdk:11.0.10-jre-slim
ARG GIT_VERSION
ARG GIT_DATE

# add jar to image
RUN echo "$GIT_VERSION,$GIT_DATE" > version.txt
COPY ./target/service.jar service.jar

# Startup
CMD java ${JAVA_OPTS} -jar ./service.jar
