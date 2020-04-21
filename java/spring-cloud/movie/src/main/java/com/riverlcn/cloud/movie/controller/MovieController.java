package com.riverlcn.cloud.movie.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.riverlcn.cloud.movie.client.UserFeignClient;
import com.riverlcn.cloud.movie.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author river
 */
@Slf4j
@RequestMapping("/movies")
@RestController
public class MovieController {

//    @Resource
//    private RestTemplate restTemplate;
//
//    @GetMapping("/users/{id}")
//    public User findById(@PathVariable Long id) {
//        // 这里用到了RestTemplate的占位符能力
//        User user = this.restTemplate.getForObject("http://user/users/{id}", User.class, id);
//        // ...电影微服务的业务...
//        return user;
//    }


    @Resource
    private UserFeignClient userFeignClient;

    // HystrixCommand 保护该API，包括好酷哦：超时机制，仓壁模式，断路器
    // fallbackMethod 属性，指定了降级的方法，默认方案抛异常
    // 可以通过 health 查看断路器的状态
    @HystrixCommand(fallbackMethod = "findByIdFallback")
    @GetMapping("/users/{id}")
    public User findById(@PathVariable Long id) {
        return userFeignClient.findById(id);
    }

    public User findByIdFallback(Long id, Throwable throwable) {
        log.error("into fall method: ", throwable);
        return new User(id, "默认用户", "默认用户", 0, new BigDecimal(1));
    }
}
