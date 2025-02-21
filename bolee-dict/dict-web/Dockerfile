FROM openjdk:11-jdk
LABEL maintainer="研究院研发组 <research@itcast.cn>"
 
# 时区修改为东八区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
 
WORKDIR /dict-web
ARG PACKAGE_PATH=./target/dict-web.jar
ADD ${PACKAGE_PATH:-./} dict-web.jar

EXPOSE 8080

ENV JAVA_OPTS="\
-server \
-Xms256m \
-Xmx512m \
-XX:MetaspaceSize=256m \
-XX:MaxMetaspaceSize=512m\
-Dspring.profiles.active=test"
ENTRYPOINT ["sh","-c","java -Djava.security.egd=file:/dev/./urandom -jar $JAVA_OPTS dict-web.jar"]