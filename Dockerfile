FROM eclipse-temurin:17-jdk
ARG JAR_FILE
ADD "target/${JAR_FILE}.jar" "/app.jar"
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","/app.jar"]
