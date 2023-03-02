FROM openjdk:17
EXPOSE 8080/tcp
ARG JAR_FILE=target/izicap_api.jar izicap_api.jar
COPY ${JAR_FILE} izicap_api.jar
ENTRYPOINT ["java","-jar","/izicap_api.jar"]
