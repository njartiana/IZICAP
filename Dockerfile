FROM openjdk:17
EXPOSE 8080/tcp
ARG JAR_FILE=target/izicap-api.jar izicap-api.jar
COPY ${JAR_FILE} izicap-api.jar
ENTRYPOINT ["java","-jar","/izicap-api.jar"]
