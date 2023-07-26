# OnePlatform部署文档

## 1. 运行环境部署

运行环境为公有云服务器，基于Docker容器化部署。

### 1.1. 容器网络

- oneplatform-access：用于前端访问后端，连接Nginx和服务网关；
- oneplatform-net：用于后端各服务容器互相访问。

创建命令：

```shell
docker network create oneplatform-access
docker network create oneplatform-net
```

查看命令：

```shell
docker network ls
docker network inspect oneplatform-access
docker network inspect oneplatform-net
```

### 1.2. 存储目录结构

- one-platform
    - configs
        - system：存储oneplatform-system的配置文件；
        - todo：存储oneplatform-todo的配置文件；
    - logs
        - system：存储oneplatform-system的日志文件；
        - todo：存储oneplatform-todo的日志文件；
    - mysql
        - conf：存储MySQL配置文件；
        - data：存储MySQL持久化数据；
        - logs：存储MySQL日志。

### 1.3. MySQL数据库

1. 拉取MySQL 8.0.29数据库镜像：

```shell
docker pull mysql:8.0.29
```

2. 启动容器：

- 容器`/etc/mysql/conf.d`用rw模式挂载到服务器`/root/one-platform/mysql/conf`，存放配置文件；
- 容器`/var/lib/mysql`用rw模式挂载到服务器`/root/one-platform/mysql/data`存放数据；
- 容器`/var/log/mysql`用rw模式挂载到服务器`/root/one-platform/mysql/logs`存放日志；
- 绑定 9000 端口；
- 自动重启。

```shell
docker run --name oneplatform-mysql -v /root/one-platform/mysql/conf:/etc/mysql/conf.d:rw -v /root/one-platform/mysql/data:/var/lib/mysql:rw -v /root/one-platform/mysql/logs:/var/log/mysql:rw -e MYSQL_ROOT_PASSWORD=密码 -p 9000:3306 --net oneplatform-net --restart=unless-stopped -d mysql:8.0.29
```

3. 启动交互终端，输入密码登录：

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

### 1.4. Redis缓存

1. 拉取Redis 7.0.11镜像：

```shell
docker pull redis:7.0.11
```

2. 启动容器：

```shell
docker run --name oneplatform-redis --restart=unless-stopped -p 9100:6379 --net oneplatform-net -d redis:7.0.11
```

### 1.5. 服务注册中心

1. 拉取Consul 1.15.2镜像：

```shell
docker pull consul:1.15.2
```

2. 启动容器：

```shell
docker run --name oneplatform-registry -d -p 8500:8500 --net oneplatform-net --restart=unless-stopped consul:1.15.2
```

### 1.6. 服务监控中心

#### 本地构建镜像

1. 将所有yaml配置文件从resources目录移出到其他位置暂存。

2. 删除target目录，IDEA maven intall构建jar包。
3. 在项目根目录（`Dockerfile`文件所在目录）执行镜像构建命令：

```shell
docker build -t wyatt6/oneplatform-monitor:版本号 ./
```

4. 推送到dockerhub镜像仓库：

```shell
docker push wyatt6/oneplatform-monitor:版本号
```

#### 云服务器启动容器

1. 将原本resource目录中的application.yaml配置文件的环境标识改成`run`：

```yaml
sys:
  env: run
```

2. 将application.yaml和application-run.yaml上传到`.../one-platform/configs/monitor`目录。

3. 拉取镜像：

```shell
docker pull wyatt6/oneplatform-monitor:版本号
```

4. 启动容器：

```shell
docker run --name oneplatform-monitor -v /root/one-platform/configs/monitor/application.yaml:/application.yaml:rw -v /root/one-platform/configs/monitor/application-run.yaml:/application-run.yaml:rw -d -p 8001:8001/tcp --net oneplatform-net --restart=unless-stopped -e TZ="Asia/Shanghai" wyatt6/oneplatform-monitor:版本号
```

### 1.7. 公共模块

IDEA maven install构建oneplatform-common模块jar包。

### 1.8. 服务网关

#### 本地构建镜像

1. 将所有yaml配置文件从resources目录移出到其他位置暂存。

2. 删除target目录，IDEA maven install构建jar包。

3. 在项目根目录（`Dockerfile`文件所在目录）执行镜像构建命令：

```shell
docker build -t wyatt6/oneplatform-gateway:版本号 ./
```

4. 推送到dockerhub镜像仓库：

```shell
docker push wyatt6/oneplatform-gateway:版本号
```

#### 云服务器启动容器

1. 将原本resource目录中的application.yaml配置文件的环境标识改成`run`：

```yaml
sys:
  env: run
```

2. 将application.yaml和application-run.yaml上传到`.../one-platform/configs/gateway`目录。

3. 拉取镜像：

```shell
docker pull wyatt6/oneplatform-gateway:版本号
```

4. 启动容器：

```shell
docker run --name oneplatform-gateway -v /root/one-platform/configs/gateway/application.yaml:/application.yaml:rw -v /root/one-platform/configs/gateway/application-run.yaml:/application-run.yaml:rw -d -p 8000:8000/tcp --net oneplatform-net --restart=unless-stopped -e TZ="Asia/Shanghai" wyatt6/oneplatform-gateway:版本号
```

5. 接入前后端访问网络oneplatform-access：

```shell
docker network connect oneplatform-access oneplatform-gateway
```

### 1.9. 系统模块

#### 本地构建镜像

1. 修改`application.yaml`将环境变量改为`run`：

```yaml
sys:
  env: local / run
```

2. 删除target目录，IDEA maven install构建jar包。
3. 在项目根目录（`Dockerfile`文件所在目录）执行镜像构建命令：

```shell
docker build -t wyatt6/oneplatform-system:版本号 ./
```

4. 推送到dockerhub镜像仓库：

```shell
docker push wyatt6/oneplatform-system:版本号
```



#### 云服务器启动容器

1. 拉取镜像：

```shell
docker pull wyatt6/oneplatform-system:版本号
```

在系统模块根目录下执行以下命令，为数据库访问用户生成类似如下的加密密码和公钥：

```shell
java -cp druid-1.2.18.jar com.alibaba.druid.filter.config.ConfigTools 密码

privateKey:MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAjGZQUrI7jXmOZWzgbTYoOhq1UnaNVKns8SK3ER54U7amBc53ods68ivzMaCOE3m8mFIEL2zcERYTEowIDAQABAkASgVHI9JEA3qrHuCWM1j16WJswdP0H0bgccFqWuzp7pL6yighp0uFR897980osadnthAiEfre2rchWiP4FmuX1bVYPwzIEWnAMnCVCWzPkCIQCNFsLzCQ6B9oFNC7JpVtpKo57OKHu4vOBizcEDR0bfgwlJDFZAiB3H66UPlE1gLaMHQU6NYqive8jQXZNvA85vP3EXTHBQIhAJnbe7IrYgDpbJPGzLb2gpwjjpeTAGqIWRJ
publicKey:MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAfsafdsO415jvpWVs4G0NJKmokmkO54J2jVSpirPEitxEeeFO2pgXOd6HbOvIr8zGgjhN5vJhSBC9s3BEWExKIL==
password:NaJdfsMUDgD5M7FzRDGTekPxWoVORAUw29s0mkGIkksliEUurRp2n4oOyANqNkAgr6ZrwPbXC4ML4fJg==
```

上传配置模板`application-run.yaml`到`/root/one-platform/configs/system`目录下，并将前一步生成的公钥和密码填入配置文件：

```yaml
password: NaJdfsMUDgD5M7FzRDGTekPxWoVORAUw29s0mkGIkksliEUurRp2n4oOyANqNkAgr6ZrwPbXC4ML4fJg==
public-key: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAfsafdsO415jvpWVs4G0NJKmokmkO54J2jVSpirPEitxEeeFO2pgXOd6HbOvIr8zGgjhN5vJhSBC9s3BEWExKIL==
```

启动容器：

```shell
docker run --name oneplatform-system -v /root/one-platform/configs/system/application-run.yaml:/application-run.yaml:rw -v /root/one-platform/logs/system:/log:rw -d -p 8002:8002/tcp --net oneplatform-net --restart=unless-stopped -e TZ="Asia/Shanghai" wyatt6/oneplatform-system:版本号
```

### 1.11. 待办模块

#### 本地构建镜像

1. 将所有yaml配置文件从resources目录移出到其他位置暂存。

2. 删除target目录，IDEA maven install构建jar包。

3. 在项目根目录（`Dockerfile`文件所在目录）执行镜像构建命令：

```shell
docker build -t wyatt6/oneplatform-todo:版本号 ./
```

4. 推送到dockerhub镜像仓库：

```shell
docker push wyatt6/oneplatform-todo:版本号
```

#### 云服务器启动容器

1. 将原本resource目录中的application.yaml配置文件的环境标识改成`run`：

```yaml
sys:
  env: run
```

2. 将application.yaml和application-run.yaml上传到`.../one-platform/configs/todo`目录。

3. 拉取镜像：

```shell
docker pull wyatt6/oneplatform-todo:版本号
```

4. 启动容器：

```shell
docker run --name oneplatform-todo -v /root/one-platform/configs/todo/application.yaml:/application.yaml:rw -v /root/one-platform/configs/todo/application-run.yaml:/application-run.yaml:rw -v /root/one-platform/logs/todo:/log:rw -d -p 8004:8004/tcp --net oneplatform-net --restart=unless-stopped -e TZ="Asia/Shanghai" wyatt6/oneplatform-todo:版本号
```
