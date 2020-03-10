FROM docker-prod.registry.kroger.com/library/java-openjre:11-latest

EXPOSE 8080
WORKDIR /opt/app

# JAR_NAME needs to be passed during the dockerize...
# Example: docker build --build-arg JAR_NAME=foo.jar .
ARG JAR_NAME=service.jar

# Deployment pipeline should override with appropriate environment profile
ENV SPRING_PROFILES_ACTIVE=dev

COPY target/${JAR_NAME} /opt/app/service.jar

CMD ["java","-jar","/opt/app/service.jar"]
