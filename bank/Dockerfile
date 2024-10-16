FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/tunabank-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} tunabank-springboot.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/tunabank-springboot.jar"]
