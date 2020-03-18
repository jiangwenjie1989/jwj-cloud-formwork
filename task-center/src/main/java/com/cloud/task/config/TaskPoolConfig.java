package com.cloud.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * proxy-target-class属性值决定是基于接口的还是基于类的代理被创建。
 * 如果proxy-target-class 属性值被设置为true，那么基于类的代理将起作用
 * （这时需要cglib库）。如果proxy-target-class属值被设置为false或者这个
 * 属性被省略，那么标准的JDK 基于接口的代理
 *
 */
@EnableAsync(proxyTargetClass=true)
@Configuration
public class TaskPoolConfig {
	
	@Bean
	public Executor taskExecutor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  //线程池维护线程的最少数量
        executor.setMaxPoolSize(20);  //线程池维护线程的最大数量
        executor.setKeepAliveSeconds(3000);//线程池维护线程所允许的空闲时间
        executor.setQueueCapacity(100);  //线程池所使用的缓冲队列 
        executor.setThreadNamePrefix("taskExecutor-"); 
        executor.setWaitForTasksToCompleteOnShutdown(true);//优雅关闭
        executor.initialize();  
        return executor;  
	}
}
