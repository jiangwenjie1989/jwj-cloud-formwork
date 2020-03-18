package com.cloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @ClassName : ConfigCenterApplication  //类名
 * @Description : 配置中心  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-02 13:00  //时间
 */
@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication
public class ConfigCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigCenterApplication.class, args);
    }

}
