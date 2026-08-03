# 基础镜像（用官方 Java 镜像，选对应版本，alpine 是轻量版）
FROM openjdk:17-ea-jdk-slim
# 将本地 JAR 包复制到容器内，并重命名为 app.jar
COPY target/redisDemo-0.0.1-SNAPSHOT.jar /app.jar
# 暴露端口（对应 Spring Boot 项目的端口）
EXPOSE 8080
# 容器启动时执行的命令
ENTRYPOINT ["java","-jar","/app.jar", "--spring.profiles.active=test"]