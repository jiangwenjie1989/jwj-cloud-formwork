package com.cloud.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @ClassName : MessageCenterApplication  //类名
 * @Description : 消息中心  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-02 13:42  //时间
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaRepositories(basePackages="com.cloud.message.repository")
@EntityScan(basePackages="com.cloud.model.message")
@ComponentScan(basePackages="com.cloud.*")
public class MessageCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageCenterApplication.class, args);
    }

}