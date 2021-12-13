FROM java:8
VOLUME /tmp
ADD WxSpringBoot-0.1.2.jar suishoubang.jar
ENV LANG C.UTF-8
ENV LANGUAGE zh_CN.UTF-8
ENV LC_ALL C.UTF-8
ENV TZ Asia/Shanghai
EXPOSE 4433
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/suishoubang.jar"]
