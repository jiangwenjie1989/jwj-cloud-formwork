package com.cloud.mini.config;

import com.cloud.mini.interceptor.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * @ClassName : CommonWebConfig  //类名
 * @Description : 拦截器管理类 //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 13:46  //时间
 */
@Configuration
public class CommonWebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private SessionInterceptor sessionInterceptor;

	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		/**
		 * 登陆验证拦截器
		 * url 包含 /external/ 连接表示 对外暴露的接口都不需要 验证token  zuul过滤器也有这样约定的规则
		 *
		 * url 包含 /internal/ 连接表示 对内暴露的接口都不需要 验证token
		 */
		registry.addInterceptor(sessionInterceptor).addPathPatterns("/admin/**")
		.excludePathPatterns("/admin/external/**","/admin/internal/**");


		
	}
	
}
