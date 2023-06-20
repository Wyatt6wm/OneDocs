# OnePlatfrom开发指南

## 服务注册

### 1. 引入依赖包

```xml
<!-- Spring Boot Actuator服务注册和服务监控都用到 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<!-- 服务注册中心客户端  -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>
```

### 2. 添加配置

`application.yaml`

```yaml
spring:
  application:
    name: 微服务名称（应用名称）
  cloud:
    # 服务注册
    consul:
      host: ${sys.op-registry.host}
      port: ${sys.op-registry.port}
      discovery:
        serviceName: ${spring.application.name}
```

`application-dev.yaml`

```yaml
sys:
  op-registry:
    host: localhost
    port: 8500
    domain: ${sys.op-registry.host}:${sys.op-registry.port}
```

`application-local.yaml`和`application-run.yaml`

```yaml
sys:
  op-registry:
    host: oneplatform-registry
    port: 8500
    domain: ${sys.op-registry.host}:${sys.op-registry.port}
```

### 3. 启动类添加注解

```java
@EnableDiscoveryClient
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

