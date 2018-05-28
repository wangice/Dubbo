#!/bin/bash
### BEGIN INIT INFO
# Provides:          ignite-cache-preloading
# Required-Start:    $local_fs $remote_fs $network $syslog
# Required-Stop:     $local_fs $remote_fs $network $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: starts the Ignite.Preloading
# Description:       starts ignite-cache-preloading using start-stop-daemon
### END INIT INFO

########################################################################################
#ignite-cache-preloading目录结构:
#/opt/ignite-cache-preloading
#            |________app
#            |________log
########################################################################################

# 当前目录
Dir=$(cd "$(dirname "$0")"; pwd)
#安装文件
JAR_File_Name=dubbo-component-ignite-preloading.jar
#启动/停止脚本
JAR_Shell=dubbo-component-ignite-preloading.sh
#应用主目录
App_Base_Home=/opt/dubbo-component-ignite-preloading
#应用程序安装目录
App_Home=/opt/dubbo-component-ignite-preloading/app
#应用程序日志目录
Log_Home=/opt/dubbo-component-ignite-preloading/log
#系统服务名称
App_Service_Name=dubbo-component-ignite-preloading
#应用程序备份目录
Backup_Home=/opt/dubbo-component-ignite-preloading/backup
#Ignite设置
IGNITE_HOME=/opt/apache-ignite-fabric-2.1.0-bin

#休眠等待次数
SleepTime=300
#默认SpringBoot激活环境 默认prod
Profile=prod

JvmArgs="-Djava.net.preferIPv4Stack=true -XX:+UseG1GC -Xms512m -Xmx512m -XX:MaxMetaspaceSize=256m -XX:+DisableExplicitGC -XX:+AggressiveOpts"

DUBBO_SERVER_PORT=20880
