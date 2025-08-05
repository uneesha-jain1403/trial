# install base image
FROM openjdk:17

# expose the port number
EXPOSE 8080

# steps involve before programs executes
ADD target/accounts.jar accounts.jar

# execute the commands
CMD ["java", "-jar", "/accounts.jar"]