FROM openjdk:11-jdk
LABEL maintainer="研究院研发组 <research@itcast.cn>"
 
# 时区修改为东八区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
 
WORKDIR /insurance-mgt
ARG PACKAGE_PATH=./target/insurance-mgt.jar
ADD ${PACKAGE_PATH:-./} insurance-mgt.jar


EXPOSE 8080

ENV JAVA_OPTS="\
-server \
-Xms256m \
-Xmx512m \
-XX:MetaspaceSize=256m \
-XX:MaxMetaspaceSize=512m\
-Dspring.profiles.active=test"
ENTRYPOINT ["sh","-c","java -Djava.security.egd=file:/dev/./urandom -jar $JAVA_OPTS insurance-mgt.jar"]