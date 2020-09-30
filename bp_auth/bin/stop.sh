#!/bin/sh
#cat pidfile|xargs kill
#rm pidfile
ps -ef|grep "authentication" |grep java |grep -v grep |awk -F" " '{ print $2}' |xargs -i kill -9  {}
