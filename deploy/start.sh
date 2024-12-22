#!/bin/bash

MODULE=start

res=$(ps -ef | grep java | grep -v 'grep' | grep ${MODULE} | awk -F ' ' '{print $2}')
if [ "" != "$res" ]; then
  $(kill -9 $res)
  echo "killed progress:$res"
fi

APP_ENV=dev

JAR_FILE_NAME=$MODULE.jar

echo "start $JAR_FILE_NAME ..."

#if [ "" == "${JAVA_OPT}" ] ;then
## 设置默认启动参数
#    JAVA_OPT="-server -Xms15g -Xmx15g -Xmn2048m -XX:+UseG1GC -XX:G1HeapRegionSize=32m -XX:G1ReservePercent=25 -XX:InitiatingHeapOccupancyPercent=30 -XX:SoftRefLRUPolicyMSPerMB=0 -verbose:gc -Xlog:gc*=info:file=gclogs/gc.log:time:filecount=10,filesize=100M -XX:-OmitStackTraceInFastThrow -XX:+AlwaysPreTouch -XX:MaxDirectMemorySize=4g -XX:-UseLargePages -XX:-UseBiasedLocking"
#fi

exec java -jar $JAR_FILE_NAME --spring.profiles.active=$APP_ENV >>logs/$MODULE.log 2>&1