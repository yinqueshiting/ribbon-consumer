package com.example.ribbonconsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @description: 服务消费
 * @create: 2019-12-16 17:11
 **/
@RestController
public class ConsumerController {
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(value = "initialConsumer")
    public String initialConsumer(){
        //POST-IN-EUREKA-SERVER-NO1 服务提供者的 application.name
        // initial是RequestMapping
        //这里不直接写Ip地址也说明 服务治理是对各个实例的自动化发现与注册
        return restTemplate.getForEntity("http://POST-IN-EUREKA-SERVER-NO1/initial", String.class).getBody();
    }
}
