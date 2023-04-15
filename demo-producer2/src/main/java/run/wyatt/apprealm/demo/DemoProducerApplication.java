package run.wyatt.apprealm.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Wyatt
 * @date 2023/4/16 0:38
 */
@EnableDiscoveryClient
@SpringBootApplication
public class DemoProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoProducerApplication.class, args);
    }
}
