package com.cloud.file.exception;


import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


	/**
	 * 默认异常处理
	 * @param
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	public SimpleResponse defaultHandlerExceptionResolverHandler(Exception e) {
		SimpleResponse simpleResponse = new SimpleResponse();
		if(e instanceof BusinessException) {
			BusinessException businessException = (BusinessException)e;
			if ( businessException != null ) {
				simpleResponse.setStatus(SysRespStatusE.失败.getDesc());
				simpleResponse.setCode(businessException.getCode());
				simpleResponse.setMessage(businessException.getMsg());
				log.error("业务异常："+"错误码："+businessException.getCode() + "，错误消息：" + businessException.getMsg());
				e.printStackTrace();
			}
			return simpleResponse;  
		}
		simpleResponse.setStatus(SysRespStatusE.失败.getDesc());
		simpleResponse.setCode(HttpCodeE.内部错误.value);
		simpleResponse.setMessage("网络异常请求稍后再试!");
		e.printStackTrace();
		return simpleResponse;
	}

}
