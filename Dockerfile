# Start from Java image
FROM openjdk:21-jdk-slim

# Add metadata
LABEL maintainer="vismit0827hi@gmail.com"

# Set working directory
WORKDIR /app

# Copy jar file (replace with your actual jar name)
COPY target/firstjobapp-0.0.1-SNAPSHOT.jar app.jar

# Expose port (change if you use different port)
EXPOSE 8085

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
