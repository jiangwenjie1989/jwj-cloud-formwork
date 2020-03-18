package com.cloud.user.interceptor;


import com.alibaba.fastjson.JSON;
import com.cloud.common.redis.RedisStringCacheSupport;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.common.utils.ThreadLocalUtil;
import com.cloud.common.utils.TokenUtil;
import com.cloud.common.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName : AppSessionInterceptor  //类名
 * @Description : 权限拦截器 //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-05 13:46  //时间
 */
@Component
public class AppSessionInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private RedisStringCacheSupport cacheString;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token=request.getHeader("token");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		if(!ValidationUtils.isStrsNull(token)){
			if(!TokenUtil.verifyToken(token)){
				return writerMessage(response,HttpCodeE.token有误.value,"token有误或者过期");
			}
			String userId = TokenUtil.getField(token, "userId");
			ThreadLocalUtil.setUserIdHolder(Long.parseLong(userId));
		}else {
			return writerMessage(response,HttpCodeE.token为空.value,"token为空");
		}
		return true;
	}

	private boolean writerMessage(HttpServletResponse response,Integer code,String message) throws IOException {
		SimpleResponse resp = new SimpleResponse();
		PrintWriter pw = response.getWriter();
		resp.setCode(code);
		resp.setStatus(SysRespStatusE.失败.getDesc());
		resp.setMessage(message);
		pw.print(JSON.toJSONString(resp));
		pw.flush();
		pw.close();
		return false;
	}

}
