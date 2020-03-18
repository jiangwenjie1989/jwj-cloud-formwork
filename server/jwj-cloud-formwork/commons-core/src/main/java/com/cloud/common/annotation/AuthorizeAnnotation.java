package com.cloud.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志注解
 * 
 * @author jiangwenjie
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizeAnnotation {

	/**
	 * 权限资源
	 * @return
	 */
	String hasAuthority();


}
