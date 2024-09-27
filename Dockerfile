FROM java:17
LABEL authors="tgkhanhdev"

EXPOSE 8085

ADD target/chatApp-server.jar chatApp-server.jar

ENTRYPOINT ["java", "-jar ", "chatApp-server.jar"]