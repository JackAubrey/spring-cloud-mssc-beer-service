# Master  [![CircleCI](https://dl.circleci.com/status-badge/img/gh/JackAubrey/spring-cloud-mssc-beer-service/tree/master.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/JackAubrey/spring-cloud-mssc-beer-service/tree/master)
# Develop [![CircleCI](https://dl.circleci.com/status-badge/img/gh/JackAubrey/spring-cloud-mssc-beer-service/tree/develop.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/JackAubrey/spring-cloud-mssc-beer-service/tree/develop)
# MSSC Beer Service

Source code in this repository is to support my online courses.

Learn more about my courses below!
* [Spring Boot Microservices with Spring Cloud](https://www.udemy.com/spring-boot-microservices-with-spring-cloud-beginner-to-guru/?couponCode=GIT_HUB2)


# Default Port Mappings - For Single Host
| Service Name | Port | 
| --------| -----|
| Brewery Beer Service | 8080 |
| [Brewery Beer Order Service](https://github.com/springframeworkguru/mssc-beer-order-service) | 8081 |
| [Brewery Beer Inventory Service](https://github.com/springframeworkguru/mssc-beer-inventory-service) | 8082 |


## Commands used to push to DockerHub and manage the version:
* mvn release:prepare
* mvn release:perform
* git checkout tags/<TAG>
* mvn clean package -DskipTests docker:build docker:push