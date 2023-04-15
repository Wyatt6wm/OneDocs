package run.wyatt.apprealm.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Wyatt
 * @date 2023/4/5 20:13
 */
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
public class OneMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(OneMonitorApplication.class, args);
    }
}
