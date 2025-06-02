FROM eclipse-temurin:22-jdk AS buildstage 
 
RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml .
COPY src /app/src
COPY Wallet_J4MXCG39LLG5V7MK /app/wallet

ENV TNS_ADMIN=/app/wallet

RUN mvn clean package -DskipTests

FROM eclipse-temurin:22-jdk 

COPY --from=buildstage /app/target/microservicio-0.0.1-SNAPSHOT.jar /app/microservicio.jar

COPY Wallet_J4MXCG39LLG5V7MK /app/wallet

ENV TNS_ADMIN=/app/wallet
EXPOSE 8080

ENTRYPOINT [ "java", "-jar","/app/microservicio.jar" ]



