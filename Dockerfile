FROM openjdk:11

WORKDIR /app

COPY ./*.java /app/

RUN javac ChatServer.java ChatClient.java UserThread.java ReadThread.java WriteThread.java