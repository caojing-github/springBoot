# 该镜像需要依赖的基础镜像
FROM openjdk:8-jdk
# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 声明服务运行在10083端口
EXPOSE 10083
# 指定维护者的名字
MAINTAINER caojing
# 指定docker容器启动时运行jar包
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
# 将当前目录下的jar包复制到docker容器的/目录下
ADD target/springboot-banner-0.0.1.jar /app.jar
