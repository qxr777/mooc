PID=$(ps -ef | grep mooc-class-2.1.2.RELEASE.jar | grep -v grep | awk '{ print $2 }')
if [ -z "$PID" ];then
    echo Application is already stopped
else
    echo kill $PID
    kill $PID
fi
