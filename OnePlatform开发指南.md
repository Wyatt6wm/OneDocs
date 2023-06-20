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

## 服务监控

### 1. 引入依赖包

```xml
<!-- ========== 微服务框架 ========== -->
<!-- Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
</dependency>
<!-- Spring Boot Actuator服务注册和服务监控都用到 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<!-- 服务监控中心客户端 -->
<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-client</artifactId>
    <version>2.5.2</version>
</dependency>
```

### 2. 添加配置

`application.yaml`

```yaml
spring:
  application:
    name: 微服务名称（应用名称）
  boot:
    # 服务监控
    admin:
      client:
        url: http://${sys.op-monitor.domain}
# 为服务监控中心开放健康检查接口，即对/actuator/*路径的访问
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: ALWAYS
```

`application-dev.yaml`

```yaml
sys:
  op-monitor:
    host: oneplatform-monitor
    port: 8001
    domain: ${sys.op-monitor.host}:${sys.op-monitor.port}
```

`application-local.yaml`和`application-run.yaml`

```yaml
sys:
  op-monitor:
    host: localhost
    port: 8001
    domain: ${sys.op-monitor.host}:${sys.op-monitor.port}
```

