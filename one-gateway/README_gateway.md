注意：当前最新版本：`1.1`，构建和部署时应把下面的命令中的标签替换成目标版本号。

# 构建镜像

使用Dockerfile创建镜像并推送到dockerbub：

```shell
docker build -t wyatt6/one-gateway:1.0 ./
docker push wyatt6/one-gateway:1.0
```

# 部署运行

拉取镜像：

```shell
docker pull wyatt6/one-gateway:1.0
```

运行镜像容器：

```shell
docker run --name one-gateway -d -p 8010:8010/tcp --restart=unless-stopped -e TZ="Asia/Shanghai" wyatt6/one-gateway:1.0
```

如果没有创建后台网络，则：

```shell
docker network create back-net
```

后台服务加入网络并重启容器

```shell
docker network connect back-net one-gateway
docker restart one-gateway
```

