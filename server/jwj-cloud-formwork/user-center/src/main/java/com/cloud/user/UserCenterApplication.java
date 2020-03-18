package com.cloud.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @ClassName : UserCenterApplication  //类名
 * @Description : 用户中心  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-02 13:42  //时间
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaRepositories(basePackages="com.cloud.user.repository")
@EntityScan(basePackages="com.cloud.model.user")
@ComponentScan(basePackages="com.cloud.*")
public class UserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }

}