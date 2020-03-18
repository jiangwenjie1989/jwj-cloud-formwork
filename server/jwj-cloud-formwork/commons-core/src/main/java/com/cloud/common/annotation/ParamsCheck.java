package com.cloud.common.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface ParamsCheck {

	/**
	 * 指定要检查的参数名称，默认全部检查
	 * @return
	 */
	String[] names() default {};
}
