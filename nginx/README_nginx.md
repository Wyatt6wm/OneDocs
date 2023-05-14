# 1. Nginx的简单使用说明

在[https://hub.docker.com/](https://hub.docker.com/)镜像仓库搜索Nginx官方发布的Nginx镜像。阅读Nginx镜像的使用说明，获取以下有用信息：

1. Nginx镜像中存储诸如404提示页面等静态页面的目录为`/usr/share/nginx/html`，如果想用自己的写的静态页面则需要挂在本地目录到该目录，或者在Dockerfile中将自己的静态页面目录复制到该目录并构建新镜像。
2. 要做端口映射，将入口Web端口（http 80/https 443）映射到Nginx容器的端口。
3. Nginx镜像中的配置文件路径为`/etc/nginx/nginx.conf`，如果要用自定义的配置则需要挂在本地的配置文件到该路径，或者在Dockerfile中将自己的配置文件复制到该路径并构建新镜像，否则将使用镜像内置的默认配置文件。
4. Nginx的1.19新版本开始支持环境变量配置的方式，详见官方文档，这里暂不需要用到这个特性。
5. Nginx还可以用只读模式或调试模式运行，但是这里暂不需要用到这个特性。
6. 还有其他诸如非root运行Nginx的这里也暂不需要，因此就不写了。

# 2. 运行Nginx容器服务

拉取Nginx镜像：

```shell
docker pull nginx:1.21.6
```

## 2.1. 云服务器上运行

在云服务商为自己的域名申请免费SSL证书，下载证书文件（下载时选择服务器类型为Nginx即可），获得下面的4种文件上传到轻量应用服务器的`/root/nginx/ssl_cert/`目录下：

- `域名_bundle.crt`证书文件

- `域名_bundle.pem`证书文件（可忽略该文件）

- `域名.csr`CSR文件（提供给CA机构的文件，安装时可忽略该文件）

- `域名.key`私钥文件

执行命令运行Nginx容器，以达到以下的目的：

- 容器名直接叫`nginx`；
- 后台运行；
- 将容器`/etc/nginx/ssl_cert`用rw模式挂载到本地的`/root/nginx/ssl_cert`（SSL证书同步）；
- 将容器`/etc/nginx/nginx.conf`用rw模式挂载到本地的`/root/nginx/nginx.conf`（配置文件同步）；
- 将容器`/usr/share/nginx/html`用rw模式挂载到本地的`/root/nginx/html`（静态网页同步）；
- 将容器`/var/log/nginx`用rw模式挂载到本地的`/root/nginx/log`（日志同步）；
- 映射端口：主机80→nginx容器80，TCP协议；主机443→nginx容器443，TCP协议；
- 除非手动停止容器，否则自动重启（比如重启docker系统服务后自动重启容器）；
- 时区设置成东8区（Asia/Shanghai）。

```shell
docker run --name nginx -d -v /root/nginx/ssl_cert:/etc/nginx/ssl_cert:rw -v /root/nginx/nginx.conf:/etc/nginx/nginx.conf:rw -v /root/nginx/html:/usr/share/nginx/html:rw -v /root/nginx/log:/var/log/nginx:rw -p 80:80/tcp -p 443:443/tcp --restart=unless-stopped -e TZ="Asia/Shanghai" nginx:1.21.6
```

修改配置文件`/root/nginx/nginx.conf`，监听用于HTTPS服务的443端口，配置用于解密的证书和密钥文件，即可通过HTTPS协议访问。除此之外，通常还会把通过HTTP服务的80端口的访问，自动转发到443端口，详细的配置内容参见本目录下的配置文件`nginx.conf`。重启nginx容器。

## 2.2. Windows本地开发环境运行

如果不需要在本地开发，可以忽略本节内容。

从[https://github.com/FiloSottile/mkcert](https://github.com/FiloSottile/mkcert)下载`mkcert`工具，下载下来的exe文件`mkcert-v1.4.4-windows-amd64.exe`重命名为`mkcert.exe`。用它生成证书文件，参考上一节在云服务商获取的SSL证书，只需要用到crt文件和key文件，所以这里只需要为Windows本地开发环境生成这两个文件：

```powershell
.\mkcert.exe -key-file localhost.key -cert-file localhost.crt localhost
```

- `localhost.crt`证书文件
- `localhost.key`私钥文件

将这两个文件复制到用来挂在到docker的目录下，我这里是`F:\AppData\docker_mount\nginx\ssl_cert`，然后参考上一届的目的执行命令运行Nginx容器：

```powershell
docker run --name nginx -d -v F:\AppData\docker_mount\nginx\ssl_cert:/etc/nginx/ssl_cert:rw -v F:\AppData\docker_mount\nginx\nginx.conf:/etc/nginx/nginx.conf:rw -v F:\AppData\docker_mount\nginx\html:/usr/share/nginx/html:rw -v F:\AppData\docker_mount\nginx\log:/var/log/nginx:rw -p 80:80/tcp -p 443:443/tcp --restart=unless-stopped -e TZ="Asia/Shanghai" nginx:1.21.6
```

打开Windows防火墙的443端口：控制面板——系统和安全——Windows Defender 防火墙——高级设置——入站规则——新建规则——端口——下一页——TCP，特地本地端口：443——下一页——允许连接——下一页——域、专用、公用全选——下一页——名称“HTTPS端口”——完成。

配置文件也参考上一节的`nginx.conf`把服务器域名改成`localhost`即可，详见本目录下的`nginx.local.conf`。