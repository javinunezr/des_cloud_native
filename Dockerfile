FROM eclipse-temurin:21-jdk AS buildstage

RUN apt-get update && apt-get install -y maven
WORKDIR /app
COPY pom.xml .
COPY src /app/src
COPY Wallet_N72BZHZWYZGTE7OH /app/wallet/
ENV TNS_ADMIN=/app/wallet
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
COPY --from=buildstage /app/target/microservicio-0.0.1-SNAPSHOT.jar /app/microservicio.jar
COPY --from=buildstage /app/wallet /app/wallet
ENV TNS_ADMIN=/app/wallet
RUN chmod -R 755 /app/wallet
RUN mkdir -p /app/efs
EXPOSE 8080
ENTRYPOINT [ "java", "-jar","/app/microservicio.jar" ]