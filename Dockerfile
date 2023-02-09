FROM maven:3-eclipse-temurin-18-alpine AS build
WORKDIR /usr/src
ENV DEBIAN_FRONTEND=noninteractive
RUN apk add --no-cache --force-refresh -q mysql mysql-client && \
    mkdir /var/run/mysqld && chown mysql:mysql /var/run/mysqld && \
    mysql_install_db --user=mysql --datadir=/var/lib/mysql/ && \
    grep -r '^\s*skip-networking' /etc/my.cnf.d/ | cut -d: -f1 | xargs -I CNF sed -i 's/^skip-networking/#skip-networking/' CNF
COPY pom.xml /usr/src
RUN mvn dependency:go-offline
COPY src/ /usr/src/src/
RUN mvn -DskipTests verify
ENV DATABASE_HOST=localhost
ENV DATABASE_TABLE=antidemo
ENV DATABASE_USER=user
ENV DATABASE_PASSWORD=user
RUN nohup bash -c "mysqld -u mysql --port 3306 --datadir=/var/lib/mysql/ &" && sleep 3 && \
    mysql -e "CREATE USER '$DATABASE_USER'@'localhost' IDENTIFIED BY '$DATABASE_PASSWORD'; CREATE DATABASE antidemo; GRANT ALL ON antidemo.* to '$DATABASE_USER'@'localhost' ; FLUSH PRIVILEGES;" && \
    mvn verify && \
    killall mysqld

FROM eclipse-temurin:18-jre-alpine
COPY --from=build /usr/src/target/antidemo-*.war antidemo.war
ENV DATABASE_HOST=db
ENV DATABASE_TABLE=antidemo
ENV DATABASE_USER=user
ENV DATABASE_PASSWORD=user
EXPOSE 8091
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar antidemo.war"]