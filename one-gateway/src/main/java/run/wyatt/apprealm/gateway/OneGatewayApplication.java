package run.wyatt.apprealm.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author Wyatt
 * @date 2023/4/15 15:01
 */
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class OneGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(OneGatewayApplication.class, args);
    }
}
