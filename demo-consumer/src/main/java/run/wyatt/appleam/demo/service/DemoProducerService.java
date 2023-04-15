package run.wyatt.appleam.demo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Wyatt
 * @date 2023/4/16 1:04
 */
@FeignClient("demo-producer")
public interface DemoProducerService {
    @RequestMapping("/hello")
    public String hello();
}
