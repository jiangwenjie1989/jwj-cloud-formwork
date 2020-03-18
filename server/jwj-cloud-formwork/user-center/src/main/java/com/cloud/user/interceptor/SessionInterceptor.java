package com.cloud.user.interceptor;


import com.alibaba.fastjson.JSON;
import com.cloud.common.annotation.AuthorizeAnnotation;
import com.cloud.common.bean.SysPermissionInfo;
import com.cloud.common.bean.SysUserInfo;
import com.cloud.common.constants.RedisCacheKeys;
import com.cloud.common.constants.CommConstants;
import com.cloud.common.redis.RedisStringCacheSupport;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.common.utils.ThreadLocalUtil;
import com.cloud.common.utils.TokenUtil;
import com.cloud.common.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName : SessionInterceptor  //类名
 * @Description : 权限拦截器 //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-05 13:46  //时间
 */
@Component
public class SessionInterceptor extends HandlerInterceptorAdapter {

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
			String username = TokenUtil.getField(token, "username");
			ThreadLocalUtil.setUserIdHolder(Long.parseLong(userId));
			ThreadLocalUtil.setUsernameHolder(username);
			if(!username.equals(CommConstants.SUPER_ADMIN)){
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				final Method method = handlerMethod.getMethod();
				//方法上有没有权限校验标志
				Boolean flag = method.isAnnotationPresent(AuthorizeAnnotation.class);
				if(flag){
					//获取权限注解
					AuthorizeAnnotation annotation = method.getAnnotation(AuthorizeAnnotation.class);
					//获取注解上面参数
					String hasAuthority = annotation.hasAuthority();
					String cacheJson = cacheString.getCached(RedisCacheKeys.USER_ADMIN_INFO_KEY + userId);
					SysUserInfo sysUserInfo = JSON.parseObject(cacheJson, SysUserInfo.class);
					List<SysPermissionInfo> sysPermissionInfoList = sysUserInfo.getSysPermissionList();
					List<String> sysRoleIdList=sysPermissionInfoList.stream().map(sysPermissionInfo -> sysPermissionInfo.getPermission()).collect(Collectors.toList());
					if(CollectionUtils.isEmpty(sysRoleIdList)){
						return writerMessage(response,HttpCodeE.权限不足.value,"权限不足");
					}
					if(!sysRoleIdList.contains(hasAuthority)){
						return writerMessage(response,HttpCodeE.权限不足.value,"权限不足");
					}
				}
			}
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
