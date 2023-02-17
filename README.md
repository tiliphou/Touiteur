## Build and run all as containers : 

`docker compose up -d` : application is available on [http://localhost:8082](http://localhost:8082)

To stop it all (including data) : `docker compose down -v`
To rebuild and restart : `docker compose build && docker compose up -d`
To check what's happening : `docker compose stats`
Want to know more(https://www.howtogeek.com/devops/how-to-monitor-the-resource-usage-of-docker-containers/) ?

## Run Spring Boot :

To start a database : `docker compose up -d db`

`mvn spring-boot:run` starts the application on [http://localhost:8091](http://localhost:8091)

To reset the database : `docker compose down -v db && docker compose up db -d`

### Demonstration video
[Link for demonstration video](https://drive.google.com/file/d/1QTWt-t8rQOQG-DnB7ECsg7CaZgaItnWg/view?usp=sharing)
