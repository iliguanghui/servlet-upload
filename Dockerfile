FROM maven:3-eclipse-temurin-21 AS builder
COPY settings.xml /usr/share/maven/conf/settings.xml
WORKDIR /src
COPY pom.xml ./
RUN mvn dependency:go-offline -B
COPY ./src ./src
RUN mvn package -Dmaven.test.skip=true
FROM tomcat:10.1.20-jdk21-temurin-jammy
RUN sed -i 's@http://archive.ubuntu.com@https://mirrors.aliyun.com@g' /etc/apt/sources.list && \
    sed -i 's@http://security.ubuntu.com@https://mirrors.aliyun.com@g' /etc/apt/sources.list
COPY --from=builder /src/target/*.war webapps/ROOT.war