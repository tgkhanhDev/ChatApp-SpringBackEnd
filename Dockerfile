FROM java:17
LABEL authors="tgkhanhdev"

EXPOSE 8080

ADD target/chatApp-server.jar chatApp-server.jar

ENTRYPOINT ["java", "-jar ", "chatApp-server.jar"]