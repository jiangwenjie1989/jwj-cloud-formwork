package com.cloud.user.config;

import com.cloud.user.interceptor.AppSessionInterceptor;
import com.cloud.user.interceptor.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;



@Configuration
public class CommonWebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private SessionInterceptor sessionInterceptor;


	@Autowired
	private AppSessionInterceptor appSessionInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		/**
		 * 登陆验证拦截器
		 * url 包含 /external/ 连接表示 对外暴露的接口都不需要 验证token  zuul过滤器也有这样约定的规则
		 *
		 * url 包含 /internal/ 连接表示 对内暴露的接口都不需要 验证token
		 */
		registry.addInterceptor(sessionInterceptor).addPathPatterns("/api/admin/**")
		.excludePathPatterns("/api/admin/external/**","/api/admin/internal/**");

		registry.addInterceptor(appSessionInterceptor).addPathPatterns("/api/app/**")
				.excludePathPatterns("/api/app/external/**","/api/app/internal/**");
		
	}
	
}
