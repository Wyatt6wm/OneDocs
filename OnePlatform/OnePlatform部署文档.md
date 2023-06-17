# OnePlatform部署文档

## 1. 部署环境

- 本地环境`local`：本地PC、笔记本电脑的Docker。
- 生产环境`run`：远程云服务器的Docker。

## 2. 部署步骤

### 2.1. 部署MySQL数据库

1. 拉取MySQL 8.0.29数据库镜像：

```shell
docker pull mysql:8.0.29
```

2. 启动容器，

**测试数据库**满足以下要求：

- 容器`/etc/mysql/conf.d`用 rw 模式挂载到服务器`/root/one-platform/test/mysql/conf`，存放配置文件；
- 容器`/var/lib/mysql`用 rw 模式挂载到服务器`/root/one-platform/test/mysql/data`存放数据；
- 容器`/var/log/mysql`用 rw 模式挂载到服务器`/root/one-platform/test/mysql/logs`存放日志；
- 绑定 9001 端口；
- 自动重启。

```shell
docker run --name oneplatform-mysql-test -v /root/one-platform/test/mysql/conf:/etc/mysql/conf.d:rw -v /root/one-platform/test/mysql/data:/var/lib/mysql:rw -v /root/one-platform/test/mysql/logs:/var/log/mysql:rw -e MYSQL_ROOT_PASSWORD=密码 -p 9001:3306 --restart=unless-stopped -d mysql:8.0.29
```

**生产数据库**满足以下要求：

- 容器`/etc/mysql/conf.d`用 rw 模式挂载到服务器`/root/one-platform/mysql/conf`，存放配置文件；
- 容器`/var/lib/mysql`用 rw 模式挂载到服务器`/root/one-platform/mysql/data`存放数据；
- 容器`/var/log/mysql`用 rw 模式挂载到服务器`/root/one-platform/mysql/logs`存放日志；
- 绑定 9000 端口；
- 自动重启。

```shell
docker run --name oneplatform-mysql -v /root/one-platform/mysql/conf:/etc/mysql/conf.d:rw -v /root/one-platform/mysql/data:/var/lib/mysql:rw -v /root/one-platform/mysql/logs:/var/log/mysql:rw -e MYSQL_ROOT_PASSWORD=密码 -p 9000:3306 --net oneplatform-net --restart=unless-stopped -d mysql:8.0.29
```

3. 启动交互终端，输入密码登录：

**测试数据库**

```shell
docker exec -it oneplatform-mysql-test mysql -uroot -p
```

**生产数据库**

```shell
docker exec -it oneplatform-mysql mysql -uroot -p
```

4. 创建数据库：

```mysql
create database db_oneplatform;
```

5. 创建数据库访问用户并授权：

```mysql
create user 'db_access'@'%' identified by '密码';
grant all privileges on db_oneplatform.* to db_access@'%';
```

### 2.2. 部署Redis缓存

1. 拉取Redis 7.0.11镜像：

```shell
docker pull redis:7.0.11
```

2. 启动容器：

**测试环境缓存**

```shell
docker run --name oneplatform-redis-test --restart=unless-stopped -p 9101:6379 --net oneplatform-net -d redis:7.0.11
```

**生产环境缓存**

```shell
docker run --name oneplatform-redis --restart=unless-stopped -p 9100:6379 --net oneplatform-net -d redis:7.0.11
```

