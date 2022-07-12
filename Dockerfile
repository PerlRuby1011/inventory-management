FROM openjdk:17-jdk
ADD target/inventory-management.jar inventory-management.jar
ENTRYPOINT [ "java", "-jar", "/inventory-management.jar" ]
EXPOSE 8080