package com.cloud.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @ClassName : BackgroundManageApplication  //类名
 * @Description : 后台管理平台  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-02 13:42  //时间
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaRepositories(basePackages="com.cloud.admin.repository")
@EntityScan(basePackages="com.cloud.model.manage")
@ComponentScan(basePackages="com.cloud.*")
public class BackgroundManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackgroundManageApplication.class, args);
    }

}