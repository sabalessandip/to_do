# Set base image
FROM openjdk:21-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY build/libs/todoapp-1.0.0.jar app.jar

# Define arguments
ARG SPRING_DATASOURCE_URL
ARG SPRING_DATASOURCE_USERNAME
ARG SPRING_DATASOURCE_PASSWORD

# Set environment variables
ENV SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL}
ENV SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
ENV SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}

# Set spring boot application profile
ENV SPRING_PROFILES_ACTIVE=deploy

# Expose the port the application runs on
EXPOSE 5000

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]