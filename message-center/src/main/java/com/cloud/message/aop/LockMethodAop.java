package com.cloud.message.aop;

import com.cloud.common.annotation.CacheLock;
import com.cloud.common.annotation.CacheParam;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.utils.Md5Utils;
import com.cloud.message.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @ClassName : LockMethodAop  //类名
 * @Description : Lock 拦截器(AOP),重复提交(redis方案)  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 11:21  //时间
 */
@Aspect
@Configuration
public class LockMethodAop {
	
	private static final Logger logger = LoggerFactory.getLogger(LockMethodAop.class);


	@Autowired
	private RedisTemplate<String, String> lockRedisTemplate;

	@Around("execution(public * *(..)) && @annotation(com.cloud.common.annotation.CacheLock)")
	public Object interceptor(ProceedingJoinPoint pjp) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		CacheLock lock = method.getAnnotation(CacheLock.class);
		if (StringUtils.isEmpty(lock.prefix())) {
			throw new BusinessException(HttpCodeE.内部错误.value, "网络异常请求稍后再试!");
		}
		String lockKey = getLockKey(pjp);
		try {
			final Boolean success = lockRedisTemplate.opsForValue().setIfAbsent(lockKey, "1");
			if (!success) {
					throw new BusinessException(HttpCodeE.重复提交.value, "您的手速过快，请休息下!");
			}
			if ( lock.prefix().equals("a:v:a:wac") || lock.prefix().equals("a:v:c:d")) {
				lockRedisTemplate.expire(lockKey, 1, lock.timeUnit());
			} else {
				lockRedisTemplate.expire(lockKey, lock.expire(), lock.timeUnit());
			}
			try {
				return pjp.proceed();
			} catch (Throwable throwable) {
				if ( throwable != null ) {
					logger.error("业务异常错误消息：" + throwable.getMessage()); 
				} 
				throw new BusinessException(HttpCodeE.调用频繁.value, "您操作过于频繁，请稍后再试!");
			}
		} finally {
			// TODO 如果演示的话需要注释该代码;实际应该放开
			if ( !lock.prefix().equals("a:v:a:wac") && !lock.prefix().equals("a:v:c:d")) {
				lockRedisTemplate.delete(lockKey);
			}
		}
	}


	public static String getLockKey(ProceedingJoinPoint pjp) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		CacheLock lockAnnotation = method.getAnnotation(CacheLock.class);
		final Object[] args = pjp.getArgs();
		final Parameter[] parameters = method.getParameters();
		StringBuilder builder = new StringBuilder();
		// TODO 默认解析方法里面带 CacheParam 注解的属性,如果没有尝试着解析实体对象中的
		for (int i = 0; i < parameters.length; i++) {
			final CacheParam annotation = parameters[i].getAnnotation(CacheParam.class);
			if (annotation == null) {
				continue;
			}
			builder.append(lockAnnotation.delimiter()).append(args[i]);
		}
		if (StringUtils.isEmpty(builder.toString())) {
			final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
			for (int i = 0; i < parameterAnnotations.length; i++) {
				final Object object = args[i];
				final Field[] fields = object.getClass().getDeclaredFields();
				for (Field field : fields) {
					final CacheParam annotation = field.getAnnotation(CacheParam.class);
					if (annotation == null) {
						continue;
					}
					field.setAccessible(true);
					builder.append(lockAnnotation.delimiter()).append(ReflectionUtils.getField(field, object));
				}
			}
		}
		return lockAnnotation.prefix() + ":" + Md5Utils.MD5(builder.toString());
	}

}
