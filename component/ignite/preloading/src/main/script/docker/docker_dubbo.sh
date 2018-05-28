#/bin/bash!
#1.检测Docker镜像是否存在,不存在则利用当前目录下Dockerfile构建Docker镜像
#2.获取参数1,检测Dubbo服务占用端口,默认20880,如果端口未被占用，以ImageName-port命名容器，否则退出shell
#3.后台运行容器,设置容器启动环境变量DUBBO_SERVER_PORT,并挂载容器内/logs(日志目录)目录到宿主机/logs/docker/containerName/目录
ImageName=mongo-dubbo-provider
#检测镜像是否存在
ImageId=
checkImageExist(){
    id=`docker images |grep ${ImageName} |awk '{print $3}'`
    if [ -n "$id" ]; then
        ImageId=${id}
    fi
}
#运行Docker镜像
checkImageExist
echo "============================================================================================================================================================================="
echo "|  Check Docker Image named ${ImageName} ... "
if [ "$ImageId" = "" ]; then
#不存在镜像,通过当前目录下Dockerfile构建镜像
    echo "|  Docker Image named ${ImageName} Not Exist ! "
    echo "|  Starting Build Docker Image Named $ImageName"
    docker build -t ${ImageName} -f ./Dockerfile .
    echo "|  Build Image $ImageName Success! "
else
#打印ImageID
    echo "|  Docker Image named ${ImageName} Exist! [ Image ID =${ImageId} ] "
fi
#默认Dubbo服务占用端口
DUBBO_SERVER_PORT=20880
if [ -n "$1" ]; then
    DUBBO_SERVER_PORT=$1
    echo "|  Set Dubbo Port ${DUBBO_SERVER_PORT} "
    else
    echo "|  Use Default Dubbo Port ${DUBBO_SERVER_PORT} "
fi
#容器名
containerName=${ImageName}-${DUBBO_SERVER_PORT}
echo "|  Check Dubbo Port ${DUBBO_SERVER_PORT} ...."
#检测Docker容器名称是否已经被占用
container_id=`docker ps -a |grep ${DUBBO_SERVER_PORT} |awk '{print $1}'`
if [ -n "${container_id}" ]; then
    echo "|  Dubbo Port ${DUBBO_SERVER_PORT} has binded , Please use another port to export dubbo service !"
    echo "|  Exit ! "
    echo "============================================================================================================================================================================="
    exit 1
fi
#默认SpringBoot激活环境
PROFILE=dev
if [ -n "$2" ]; then
    PROFILE=$2
    echo "|  Set spring.profiles.active ${PROFILE} "
    else
    echo "|  Use spring.profiles.active ${PROFILE} "
fi
#后台运行容器,并挂载容器/logs目录到宿主机/logs/docker/$containerName目录
containerId=`docker run -d --name=${containerName} --net=host --env=DUBBO_SERVER_PORT=${DUBBO_SERVER_PORT} --env=PROFILE=${PROFILE} -v /logs/docker/${containerName}:/logs ${ImageName}`
echo "|  docker run -d --name=${containerName} --net=host --env=DUBBO_SERVER_PORT=${DUBBO_SERVER_PORT}  --env=PROFILE=${PROFILE} -v /logs/docker/${containerName}:/logs ${ImageName}"
echo "|  Docker Container started ! Name = ${containerName} Id = "${containerId}
echo "============================================================================================================================================================================="
