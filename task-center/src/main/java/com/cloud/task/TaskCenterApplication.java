package com.cloud.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName : TaskCenterApplication  //类名
 * @Description : 定时任务中心  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-02 13:42  //时间
 */
@EnableScheduling
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages="com.cloud.*")
public class TaskCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskCenterApplication.class, args);
    }

}