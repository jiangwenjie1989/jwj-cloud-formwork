package com.cloud.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 日志中心
 * 
 * @author jiangwenjie
 *
 */
@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaRepositories(basePackages="com.cloud.log.repository")
@EntityScan(basePackages="com.cloud.model.log")
@ComponentScan(basePackages="com.cloud.*")
public class LogCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogCenterApplication.class, args);
	}

}