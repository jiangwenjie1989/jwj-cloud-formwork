package com.cloud.mini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @ClassName : MiniProjectApplication  //类名
 * @Description : 启动类  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-02 13:42  //时间
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaRepositories(basePackages="com.cloud.mini.repository")
@EntityScan(basePackages="com.cloud.model.mini")
@ComponentScan(basePackages="com.cloud.*")
public class MiniProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniProjectApplication.class, args);
    }

}