FROM maven:3-eclipse-temurin-18 AS build
WORKDIR /usr/src
#ENV DEBIAN_FRONTEND=noninteractive
#RUN apt-get update && apt-get install -y mysql-client mysql-server && rm -rf /var/lib/apt/list/ && \
    grep -r '^\s*skip-networking' /etc/my.cnf.d/ | cut -d: -f1 | xargs -I CNF sed -i 's/^skip-networking/#skip-networking/' CNF
COPY pom.xml /usr/src
RUN mkdir -p $HOME/.m2 ; echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd"><mirrors><mirror><id>ensisa</id><name>Nexus ENSISA</name><url>https://nexus.cluster.ensisa.uha.fr/repository/maven-public/</url><mirrorOf>*</mirrorOf>*</mirror></mirrors><localRepository>/usr/share/maven/ref/repository</localRepository></settings>' > $HOME/.m2/settings.xml
RUN mvn dependency:go-offline
COPY src/ /usr/src/src/
RUN mvn -DskipTests verify
#ENV DATABASE_HOST=localhost
#ENV DATABASE_TABLE=antidemo
#ENV DATABASE_USER=user
#ENV DATABASE_PASSWORD=user
#RUN nohup bash -c "mysqld -u mysql --port 3306 --max_allowed_packet 32M --datadir=/var/lib/mysql/ &" && sleep 3 && \
#    mysql -e "CREATE USER '$DATABASE_USER'@'localhost' IDENTIFIED BY '$DATABASE_PASSWORD'; CREATE DATABASE antidemo; GRANT ALL ON antidemo.* to '$DATABASE_USER'@'localhost' ; FLUSH PRIVILEGES;" && \
#    mvn verify -DskipTests && \
#    killall mysqld

FROM eclipse-temurin:18-jre
COPY --from=build /usr/src/target/antidemo-*.war antidemo.war
ENV DATABASE_HOST=db
ENV DATABASE_TABLE=antidemo
ENV DATABASE_USER=user
ENV DATABASE_PASSWORD=user
EXPOSE 8091
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar antidemo.war"]
