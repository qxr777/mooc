PID=$(ps -ef | grep mooc-class-1.0.SNAPSHOT.jar | grep -v grep | awk '{ print $2 }')
if [ -z "$PID" ];then
    echo Application is already stopped
else
    echo kill $PID
    kill $PID
fi
