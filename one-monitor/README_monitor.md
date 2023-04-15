注意：当前最新版本：`1.2`，构建和部署时应把下面的命令中的标签替换成目标版本号。

# 构建镜像

使用Dockerfile创建镜像并推送到dockerbub：

```shell
docker build -t wyatt6/one-monitor:1.0 ./
docker push wyatt6/one-monitor:1.0
```

# 部署运行

拉取镜像：

```shell
docker pull wyatt6/one-monitor:1.0
```

运行镜像容器：

```shell
docker run --name one-monitor -d -p 8000:8000/tcp --restart=unless-stopped -e TZ="Asia/Shanghai" wyatt6/one-monitor:1.0
```
