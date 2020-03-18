package com.cloud.gateway.config;

import com.cloud.common.constants.MessageQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * rabbitmq配置
 * 
 * @author jiangwenjie
 *
 */
@Configuration
public class RabbitmqConfig {

	/**
	 * 声明队列
	 * 
	 * @return
	 */
	@Bean
	public Queue logQueue() {
		Queue queue = new Queue(MessageQueue.BLACK_IP_QUEUE);

		return queue;
	}
}
