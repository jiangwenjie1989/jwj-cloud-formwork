package com.cloud.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @ClassName : RegisterCenterApplication  //类名
 * @Description : 注册中心  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-02 12:34  //时间
 */
@EnableEurekaServer
@SpringBootApplication
public class RegisterCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegisterCenterApplication.class, args);
    }

}
