package run.wyatt.appleam.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Wyatt
 * @date 2023/4/16 0:51
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class DemoConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoConsumerApplication.class, args);
    }
}
