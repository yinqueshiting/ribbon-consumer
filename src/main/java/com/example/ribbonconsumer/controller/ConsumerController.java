package com.example.ribbonconsumer.controller;

import com.example.ribbonconsumer.server.HystrixServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 服务消费
 * @create: 2019-12-16 17:11
 **/
@RestController
@Slf4j
public class ConsumerController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HystrixServer hystrixServer;

    @PostMapping(value = "initialConsumer")
    public String initialConsumer(){
        //POST-IN-EUREKA-SERVER-NO1 服务提供者的 application.name
        // initial是RequestMapping
        //这里不直接写Ip地址也说明 服务治理是对各个实例的自动化发现与注册
        return restTemplate.getForEntity("http://POST-IN-EUREKA-SERVER-NO1/initial", String.class).getBody();
    }

    @GetMapping(value = "hystrixConsumer")
    public Map hystrixConsumer(){
        return hystrixServer.hystrixlConsumer();
    }

    @GetMapping(value = "hystrixConsumerTryException")
    public Map hystrixConsumerTryException(){
        try {
            return hystrixServer.hystrixConsumerTryException();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map resMap = new HashMap();
        resMap.put("error", "错误！");
        return resMap;
    }
    @GetMapping(value = "HystrixConsumerSelect")
    public Map HystrixConsumerSelect(){
       return hystrixServer.HystrixConsumerSelect();
    }
    @GetMapping(value = "HystrixConsumerUpdate")
    public Map HystrixConsumerUpdate(){
        return hystrixServer.HystrixConsumerUpdate();
    }

    @PostMapping(value = "sendPostJson")
    public void sendPostJosn(){
       HttpHeaders httpHeaders = new HttpHeaders();
       httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
       MultiValueMap<String,String> paramMap = new LinkedMultiValueMap<>();
       paramMap.add("key1", "value1");
       paramMap.add("key2", "value2");
       log.info("sendPostJson请求头：{}",httpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
       HttpEntity httpEntity = new HttpEntity(paramMap, httpHeaders);
       ResponseEntity responseEntity = restTemplate.postForEntity("http://POST-IN-EUREKA-SERVER-NO1/postJson", paramMap, Object.class);
       log.info("sendPostJosn得到的数据:{}",responseEntity.getBody());
    }
    @PostMapping(value = "sendPostForm")
    public void sendPostForm(){
        //HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        MultiValueMap<String,String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("key", "value1");
        //log.info("sendpostForm请求头：{}",httpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
        //HttpEntity httpEntity = new HttpEntity(paramMap, httpHeaders);
        ResponseEntity responseEntity = restTemplate.postForEntity("http://POST-IN-EUREKA-SERVER-NO1/postForm", paramMap, Object.class);
        log.info("sendPostForm得到的数据:{}",responseEntity.getBody());
    }
}
