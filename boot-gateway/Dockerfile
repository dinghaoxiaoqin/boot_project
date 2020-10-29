FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
EXPOSE 2080
ENTRYPOINT ["java","-Dfile.encoding=utf-8","-jar","/app.jar"]