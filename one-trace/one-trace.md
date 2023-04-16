# 链路追踪中心

ZipKin + Spring Cloud Sleuth（客户端服务使用），Sleuth采集数据，ZipKin存储数据（内存模式），ZipKin UI可视化显示。

拉取ZipKin镜像：

```shell
docker pull openzipkin/zipkin:2.24
```

运行ZipKin容器：

```shell
docker run --name one-trace -d -p 9411:9411 --restart=unless-stopped openzipkin/zipkin:2.24
```

如果没有创建后台网络，则：

```shell
docker network create back-net
```

后台服务加入网络并重启容器

```shell
docker network connect back-net one-trace
docker restart one-trace
```

