#!/bin/sh

#sudo setsid nohup java -jar opticoms-nms-backend-0.0.1-SNAPSHOT.jar --spring.config.location=classpath:/application-dev.yml /tmp 2>> /dev/null >> /dev/null &

ACTIVE_PROFILE=classpath:/application-dev.yml
SERVICE_NAME=NMS-Lite-Service
PATH_TO_JAR=nms-lite-0.0.1-SNAPSHOT.jar
PID_PATH_NAME=/tmp/nms-lite-service-pid
case $1 in
    start)
        echo "Starting $SERVICE_NAME ..."
        if [ ! -f $PID_PATH_NAME ]; then
            sudo setsid nohup java -jar $PATH_TO_JAR --spring.config.location=$ACTIVE_PROFILE /tmp 2>> /dev/null >> /dev/null &
            echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is already running ..."
        fi
    ;;
    stop)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stoping ..."
            sudo kill "$PID";
            echo "$SERVICE_NAME stopped ..."
            sudo rm $PID_PATH_NAME
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
    restart)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stopping ...";
            sudo kill "$PID";
            echo "$SERVICE_NAME stopped ...";
            sudo rm $PID_PATH_NAME
            echo "$SERVICE_NAME starting ..."
            sudo setsid nohup java -jar $PATH_TO_JAR --spring.config.location=$ACTIVE_PROFILE /tmp 2>> /dev/null >> /dev/null &
            echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
esac