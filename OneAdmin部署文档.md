# OneAdmin部署文档

## 1. 运行环境部署

运行环境为公有云服务器，基于Docker容器化部署。

### 1.1. 容器网络

- oneplatform-access：用于前端访问后端，连接Nginx和服务网关。

创建命令：

```shell
docker network create oneplatform-access
```

查看命令：

```
docker network ls
docker network inspect oneplatform-access
```

### 1.2. 存储目录结构

- nginx
    - html
        - one-admin：存放oneadmin Vue应用的dist目录。
    - log：存储Nginx运行日志；
    - nginx.conf：Nginx配置文件；
    - ssl_cert：存放。

### 1.3. Nginx反向代理

1. 拉取镜像：

```shell
docker pull nginx:1.21.6
```

2. 上传SSL证书：

在云服务商为自己的域名申请免费SSL证书，下载证书文件（下载时选择服务器类型为Nginx即可），获得下面的4种文件上传到服务器的`/root/nginx/ssl_cert/`目录下：

- `域名_bundle.crt`证书文件
- `域名_bundle.pem`证书文件（可忽略该文件）
- `域名.csr`CSR文件（提供给CA机构的文件，安装时可忽略该文件）
- `域名.key`私钥文件

3. 启动容器，以达到以下的目的：

- 容器名叫`nginx`；
- 后台运行；
- 将容器`/etc/nginx/ssl_cert`用rw模式挂载到本地的`/root/nginx/ssl_cert`（SSL证书同步）；
- 将容器`/etc/nginx/nginx.conf`用rw模式挂载到本地的`/root/nginx/nginx.conf`（配置文件同步）；
- 将容器`/usr/share/nginx/html`用rw模式挂载到本地的`/root/nginx/html`（静态网页同步）；
- 将容器`/var/log/nginx`用rw模式挂载到本地的`/root/nginx/log`（日志同步）；
- 映射端口：主机80→nginx容器80，TCP协议；主机443→nginx容器443，TCP协议；
- 接入`oneplatform-access`容器网络；
- 除非手动停止容器，否则自动重启（比如重启docker系统服务后自动重启容器）；
- 时区设置成东8区（Asia/Shanghai）。

```shell
docker run --name nginx -d -v /root/nginx/ssl_cert:/etc/nginx/ssl_cert:rw -v /root/nginx/nginx.conf:/etc/nginx/nginx.conf:rw -v /root/nginx/html:/usr/share/nginx/html:rw -v /root/nginx/log:/var/log/nginx:rw -p 80:80/tcp -p 443:443/tcp --net oneplatform-access --restart=unless-stopped -e TZ="Asia/Shanghai" nginx:1.21.6
```

### 1.4. OneAdmin

#### 本地构建项目

1. 在OneAdmin项目根目录安装软件包：

```shell
npm install --force
```

2. 修改`vue.config.js`文件将代理转发配置设置为服务网关容器：

```js
devServer: {
  proxy: {
    '/api': {
      target: 'http://oneplatform-gateway:8000/',
      changeOrigin: true // 允许跨域
    }
  }
}
```

3. 删除项目根目录中旧的dist目录后，构建项目：

```shell
npm run build
```

4. 删除云服务器`/root/nginx/html/one-admin`目录下的`dist`目录后，将构建完成后在根目录下生成的`dist`目录整个上传这里。

5. 配置nginx.conf文件：

```shell
# 错误日志打印路径及日志级别
error_log  /var/log/nginx/error.log  notice;
# 进程ID文件路径
pid        /var/run/nginx.pid;

events {
    # 进程最大连接数
    worker_connections  1024;
}

http {
    # 访问日志打印路径
    access_log  /var/log/nginx/access.log;

    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    server {
        # 监听SSL访问端口443
        listen                     443 ssl;
        # 绑定SSL证书的域名
        server_name                admin.wyatt.run;
        # 下载的证书文件
        ssl_certificate            /etc/nginx/ssl_cert/admin.wyatt.run_bundle.crt;
        # 下载的私钥文件
        ssl_certificate_key        /etc/nginx/ssl_cert/admin.wyatt.run.key;
        # SSL会话超时时间
        ssl_session_timeout        5m;
        # SSL协议版本
        ssl_protocols              TLSv1.2 TLSv1.3;
        # SSL解密器，采用OpenSSL库理解的格式
        ssl_ciphers                ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
        # 服务器cipher优先于客户端cipher
        ssl_prefer_server_ciphers  on;

        location / {
            root /usr/share/nginx/html/one-admin/dist;
            index index.html index.htm;
            try_files $uri $uri/ @router;
        }

        location @router {
            rewrite ^.*$ /index.html last;
        }
        
        # 前端请求后端API的代理转发
        location /api {
            proxy_pass http://oneplatform-gateway:8000/api;
        }
    }

    server {
        # 监听Web默认端口80
        listen  80;
        # 服务器域名
        server_name *.wyatt.run;
        # 301重定向http到https
        return 301 https://$host:$request_uri;
    }
}
```

6. 重启Nginx容器：

```shell
docker restart nginx
```

