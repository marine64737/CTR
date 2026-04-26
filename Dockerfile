# Start your image with a node base image
FROM openjdk:17-jdk-slim
WORKDIR /ksh/CTR
COPY . .
EXPOSE 8080

# Start the app using serve command
CMD [ "java", "-jar", "/app.jar" ]