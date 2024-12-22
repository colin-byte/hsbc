# 使用官方的OpenJDK 11作为基础镜像
FROM openjdk:11-jre-slim

ARG MODULE=start

COPY start.jar /data/trade/start.jar

COPY launch.sh /data/trade/launch.sh

ENV LANG zh_CN.UTF-8
ENV LANGUAGE zh_CN.UTF-8
ENV LC_ALL zh_CN.UTF-8

RUN mkdir -p /data/trade/logs  && \
    mkdir -p /data/trade/gclogs && \
    apt-get update && apt-get install -y procps && \
    apt-get install -y vim  && apt-get install -y curl && \
    chmod +x /data/trade/start.jar && \
    echo "alias ll='ls -al'" >> ~/.bashrc

WORKDIR /data/trade

EXPOSE 8080

CMD ["/bin/bash","-c","source ~/.bashrc && sh /data/trade/launch.sh"]