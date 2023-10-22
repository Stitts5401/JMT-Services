FROM openjdk:20 as builder
WORKDIR extracted
ADD target/*.jar web-service.jar
RUN java -Djarmode=layertools -jar web-service.jar extract

FROM openjdk:20 as runtime
WORKDIR application
COPY --from=builder extracted/dependencies/ ./
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
COPY --from=builder extracted/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

