#!/bin/sh
CURR_PATH=`dirname $0`
if [ $# -lt 1 ]
then
  ${CURR_PATH}/auth.sh start
elif [ $# -ge 2 ] && [ $1 == 'start-cluster' ]
then
  echo $1
  ${CURR_PATH}/master.sh $1 $2
fi
