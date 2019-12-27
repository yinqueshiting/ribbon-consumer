package com.example.ribbonconsumer.server;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @description: 测试Hystrix断路器功能
 * @create: 2019-12-19 19:24
 **/
@Service
@Slf4j
public class HystrixServer {
    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "helloFallback")
    public Map hystrixlConsumer(){
        return restTemplate.getForEntity("http://POST-IN-EUREKA-SERVER-NO1/initial", Map.class).getBody();
    }

    public Map helloFallback(){
        Map map = new HashMap();
        map.put("erroe", "错误了！");
        return map;
    }
    /*
        @HystrixCommand结合使用异步执行
     */
    @HystrixCommand
    public Future<Map> hystrixConsumer(){
        return new AsyncResult<Map>(){
            @Override
            public Map invoke(){
                return restTemplate.getForEntity("http://POST-IN-EUREKA-SERVER-NO1/initial", Map.class).getBody();
            }
        };
    }

    /*
        异常处理
     */
    @HystrixCommand(ignoreExceptions = NullPointerException.class,fallbackMethod = "fallbackhystrixConsumerTryException")
    public Map hystrixConsumerTryException()throws Exception{
        String string = null;
        log.info("获取str的长度：{}", string.length());
        int i = 5/0 ;
        Map resMap = new HashMap();
        resMap.put("key", "value");
        return resMap;
    }

    public Map fallbackhystrixConsumerTryException(Exception e){
        //assert "".equals(e.getMessage());
        log.info("进入fallbackhystrixConsumerTryException方法");
        Map resMap = new HashMap();
        resMap.put("key", "fallbackhystrixConsumerTryException");
        return resMap;
    }

    /*
        请求缓存
     */
    @HystrixCommand
    @CacheResult(cacheKeyMethod = "hystrixCache")
    public Map HystrixConsumerSelect(){
        log.info("走了方法体内");
        return restTemplate.getForEntity("http://POST-IN-EUREKA-SERVER-NO1/initial", Map.class).getBody();
    }

    @HystrixCommand
    @CacheRemove(commandKey = "HystrixConsumerSelect",cacheKeyMethod = "hystrixCache")
    public Map HystrixConsumerUpdate(){
        log.info("清除请求缓存");
        return restTemplate.getForEntity("http://POST-IN-EUREKA-SERVER-NO1/initial", Map.class).getBody();
    }

    public String hystrixCache(){
        log.info("调用HystrixConsumerCache方法");
        return "";
    }

    /*
        请求合并：微服务架构中的依赖通常通过远程调用实现，远程调用中最常见的问题就是通信消耗与连接数占用
                同时，依赖服务的线程池资源有限，将出现排队等候与响应延迟的情况。
        对此Hystrix提供了HystrixCollapser来实现按请求的合并，以减少通信消耗和线程数的占用
     */
}
