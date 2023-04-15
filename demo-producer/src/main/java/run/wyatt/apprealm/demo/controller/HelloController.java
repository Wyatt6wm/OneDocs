package run.wyatt.apprealm.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wyatt
 * @date 2023/4/16 0:41
 */
@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello() {
        return "Hello, I am demo-producer.";
    }
}
