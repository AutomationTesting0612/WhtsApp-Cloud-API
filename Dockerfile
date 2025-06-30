
FROM openjdk:17-jdk-slim-buster 
LABEL maintainer="akhil.sharma0612@gmail.com"

RUN mvn clean package -DskipTests

COPY --from=build /target/Whtsapp-Message-POC-0.0.1-SNAPSHOT.jar Whtsapp-Message-POC-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","Whtsapp-Message-POC-0.0.1-SNAPSHOT.jar"]