# 运行Consul容器

服务注册中心one-registry选用Consul，Consul在[dockerhub](https://hub.docker.com/_/consul)上就有官方容器，拉取下来并运行容器即可：

```shell
docker pull consul:1.15.2
docker run --name one-registry -d -p 8500:8500 --restart=unless-stopped consul:1.15.2
```

# 接入后台网络

如果没有创建后台网络，则：

```shell
docker network create back-net
```

后台服务加入网络并重启容器

```shell
docker network connect back-net one-registry
docker restart one-registry
```

