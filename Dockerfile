FROM openjdk:17
EXPOSE 8080/tcp
ARG JAR_FILE=target/demo-token.jar demo-token.jar
COPY ${JAR_FILE} demo-token.jar
ENTRYPOINT ["java","-jar","/demo-token.jar"]
