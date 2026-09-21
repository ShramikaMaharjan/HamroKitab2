# Use an of icial lightweight OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk
# Set the working directory inside the container
WORKDIR /app
# Install Maven inside the container to build from source
RUN apt-get update && apt-get install -y maven
# Copy the entire project directory into the container
COPY . .
# Build the Spring Boot application, bypassing unit tests for deployment
RUN mvn clean package -DskipTests
# Expose the standard port for web traf ic
EXPOSE 8080
# Execute the compiled application using dynamic jar handling
CMD ["sh", "-c", "java -jar target/*.jar"]