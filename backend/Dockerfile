FROM openjdk:8-jre-alpine
COPY ./target/sl-dashboard-*-standalone.jar /app/app.jar
ENV PORT 8080
CMD ["java", "-jar", "/app/app.jar"]