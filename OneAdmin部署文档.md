# OneAdmin部署文档

## 1. 部署环境

- 本地环境`local`：本地PC、笔记本电脑的Docker。
- 生产环境`run`：远程云服务器的Docker。

## 2. 部署步骤

### 2.0. 准备工作

创建前后端访问容器网路`oneplatform-access`：

```shell
docker network create oneplatform-access
```

### 2.1. 部署Nginx反向代理

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

### 2.2. 部署Windows本地Nginx反向代理

从[https://github.com/FiloSottile/mkcert](https://github.com/FiloSottile/mkcert)下载`mkcert`工具，下载下来的 exe 文件`mkcert-v1.4.4-windows-amd64.exe`重命名为`mkcert.exe`。用它生成证书文件，参考上一节在云服务商获取的SSL证书，为Windows本地开发环境生成crt和key两个文件：

```powershell
.\mkcert.exe -key-file localhost.key -cert-file localhost.crt localhost
```

- `localhost.crt`证书文件
- `localhost.key`私钥文件

将这两个文件复制到用来挂载到docker的目录下，我这里是`D:\WyattAppRealmMount\nginx\ssl_cert`，如果用自己的目录只需要调换目录名即可。执行命令运行 Nginx 容器：

```powershell
docker run --name nginx -d -v D:\WyattAppRealmMount\nginx\ssl_cert:/etc/nginx/ssl_cert:rw -v D:\WyattAppRealmMount\nginx\nginx.conf:/etc/nginx/nginx.conf:rw -v D:\WyattAppRealmMount\nginx\html:/usr/share/nginx/html:rw -v D:\WyattAppRealmMount\nginx\log:/var/log/nginx:rw -p 80:80/tcp -p 443:443/tcp --net oneplatform-access --restart=unless-stopped -e TZ="Asia/Shanghai" nginx:1.21.6
```

打开Windows防火墙的443端口：控制面板→系统和安全→Windows Defender防火墙→高级设置→入站规则→新建规则→端口→下一页→TCP，特地本地端口：443→下一页→允许连接→下一页→域、专用、公用全选→下一页→名称“HTTPS 端口”→完成。

### 2.3. 部署OneAdmin

1. 构建项目：在OneAdmin项目根目录执行`npm install --force`安装软件包，然后执行`npm run build`命令构建项目。项目构建完成后会在根目录下生成用来部署的`dist`目录，将目录整个上传到云服务器`/root/nginx/html/one-admin`目录下（如果是本地部署，则复制到`....../nginx/html/one-admin`目录下）。
2. 配置`/root/nginx/nginx.conf`文件（如果是本地部署，把`wyatt.run`改成`localhost`即可）：

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

