#!/bin/sh

### ====================================================================== ###
##                                                                          ##
##                      Auth Servive                                        ##
##                                                                          ##
### ====================================================================== ###
# ./
APP_BIN=`dirname $0`
COMM_HOME=$APP_BIN/..
export COMM_HOME
echo $COMM_HOME

JARS="${COMM_HOME}/config:"
LIB=$COMM_HOME/lib
if [ -d $LIB ]; then
  for i in $LIB/*/*.jar; do
    JARS="$JARS":$i
  done
fi
if [ -d $LIB ]; then
  for i in $LIB/*.jar; do
    JARS="$JARS":$i
  done
fi
#echo $JARS
IP=127.0.0.1
hostname=127.0.0.1
echo "hostname:"${hostname}
jmxport=14508
echo "jmxport:"${jmxport}

JAVA_OPTS="-Dlog.home=${COMM_HOME}/log -Dfile.encoding=UTF-8"
JAVA_OPTS="$JAVA_OPTS -Dprogram.name=authservice -DAPP_HOME=${COMM_HOME}"
JAVA_OPTS="$JAVA_OPTS -d64 -Xms2g -Xmx2g -Xmn1g"
JAVA_OPTS="$JAVA_OPTS -XX:PermSize=64m -XX:MaxPermSize=128m"
JAVA_OPTS="$JAVA_OPTS -XX:MaxDirectMemorySize=1024m"
JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC"
JAVA_OPTS="$JAVA_OPTS -XX:ParallelGCThreads=32"
JAVA_OPTS="$JAVA_OPTS -XX:MaxTenuringThreshold=4"
JAVA_OPTS="$JAVA_OPTS -XX:SurvivorRatio=2"
JAVA_OPTS="$JAVA_OPTS -XX:+UseConcMarkSweepGC"
JAVA_OPTS="$JAVA_OPTS -XX:-CMSParallelRemarkEnabled"
JAVA_OPTS="$JAVA_OPTS -XX:+CMSClassUnloadingEnabled"
JAVA_OPTS="$JAVA_OPTS -XX:CMSInitiatingOccupancyFraction=65"
JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSInitiatingOccupancyOnly"
JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSCompactAtFullCollection"
JAVA_OPTS="$JAVA_OPTS -XX:+DisableExplicitGC"
JAVA_OPTS="$JAVA_OPTS -Xloggc:"${DIRNAME}"/gc.vgc"
JAVA_OPTS="$JAVA_OPTS -Dsun.rmi.dgc.client.gcInterval=36000009999"
JAVA_OPTS="$JAVA_OPTS -Dsun.rmi.dgc.server.gcInterval=36000009999"
JAVA_OPTS="$JAVA_OPTS -Djava.util.Arrays.useLegacyMergeSort=true"
#JAVA_OPTS="$JAVA_OPTS -Djava.rmi.server.hostname=${hostname}"
#JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.port=${jmxport}"
#JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.ssl=false"
#JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.authenticate=false"
JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails"

echo $JAVA_OPTS

case "$1" in
	'start')
	nohup java -server $JAVA_OPTS -cp $JARS com.geovis.GeovisApplication $1 > nohup.out 2>&1 &
	echo $! >> pidfile
	;;
	'start-cluster')
	nohup java -server $JAVA_OPTS -cp $JARS  com.geovis.GeovisApplication $2 > nohup.out 2>&1 &
	;;
	'stop')
	rm ${COMM_HOME}/log/*
	rm ${COMM_HOME}/bin/nohup.out
	echo "stop finished"
	ps -ef | grep PSMS.master|grep -v grep|awk '{print $2}'|xargs -i kill -9 {}
	;;
	*)
	echo "xdpp.common , boco ltd. (c)2010-2013"
	echo "usage: ./master.sh <command>"
	echo "       command = < start | start-cluster | stop |help >"
	echo "       start: start in current shell, and in background if succeded with &."
	echo "       start-cluster: start by ice grid, and in background if succeded with &."
	echo "       help:  help information."
	;;
esac
