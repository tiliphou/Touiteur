FROM maven:3-openjdk-18-slim AS build
WORKDIR /usr/src
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y mariadb-server && rm -rf /var/cache/apt && mkdir /var/run/mysqld && chown mysql:mysql /var/run/mysqld
COPY pom.xml /usr/src
RUN mvn dependency:go-offline
COPY src/ /usr/src/src/
RUN nohup bash -c "mysqld &" && sleep 3 && \
    mysql -e "CREATE USER 'user'@'%' IDENTIFIED BY 'user'; CREATE DATABASE antidemo; GRANT ALL ON antidemo.* to 'user'@'%' ; FLUSH PRIVILEGES;" && \
    mvn verify && \
    killall mysqld

FROM eclipse-temurin:17-jdk-alpine
COPY --from=build /usr/src/target/antidemo-*.war antidemo.war
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar antidemo.war"]