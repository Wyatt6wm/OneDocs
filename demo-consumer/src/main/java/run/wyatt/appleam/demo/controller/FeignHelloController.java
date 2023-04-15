package run.wyatt.appleam.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import run.wyatt.appleam.demo.service.DemoProducerService;

/**
 * @author Wyatt
 * @date 2023/4/16 1:06
 */
@RestController
public class FeignHelloController {
    @Autowired
    private DemoProducerService demoProducerService;

    @RequestMapping("/feign/hello")
    public String hello() {
        return demoProducerService.hello();
    }
}
