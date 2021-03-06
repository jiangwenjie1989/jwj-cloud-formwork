package com.cloud.common.redis;


import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Collections;


@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * springboot 1.5以上的配置
     */
	/*@Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
		RedisCacheManager rcm=new RedisCacheManager(redisTemplate);
		
        return  rcm;
    }*/

    /**
     * springboot 2.0以上的配置
     * @param factory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheManager rcm = RedisCacheManager.builder(factory).build();
        return rcm;
    }




	
	@Bean
    public RedisSerializer<?> redisSerializer(){
		
		Jackson2JsonRedisSerializer<?> json=new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper objectMapper=new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
		objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL);
		
		objectMapper.configure(Feature.WRITE_NUMBERS_AS_STRINGS, true);
	
		json.setObjectMapper(objectMapper);
		
//		FastjsonRedisSerializer<?> json=new FastjsonRedisSerializer<>();

		return json;
    }
	
	@Bean
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory factory,RedisSerializer<?> redisSerializer) {  
        //StringRedisTemplate template = new StringRedisTemplate(factory); 
		
        RedisTemplate<String,T> template=new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setDefaultSerializer(redisSerializer);
        //template.setValueSerializer(redisSerializer);
        StringRedisSerializer stringRedisSerializer=new StringRedisSerializer();
        
        template.setStringSerializer(stringRedisSerializer);
        template.setKeySerializer(stringRedisSerializer);
        //template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        
        return template;  
    }  

//	@Bean
//	public PlatformTransactionManager txManager(DataSource datasource){
//		return new DataSourceTransactionManager(datasource);
//	}





}
