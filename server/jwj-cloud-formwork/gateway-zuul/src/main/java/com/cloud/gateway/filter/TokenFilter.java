package com.cloud.gateway.filter;


import com.alibaba.fastjson.JSON;
import com.cloud.common.constants.RedisCacheKeys;
import com.cloud.common.redis.RedisStringCacheSupport;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.common.utils.TokenUtil;
import com.cloud.common.utils.ValidationUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


/**
 * @ClassName : SecurityParmFilter  //类名
 * @Description : 验证登录token过滤器  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-03 16:07  //时间
 */
@Component
public class TokenFilter extends ZuulFilter {


    @Autowired
    private RedisStringCacheSupport cacheStr;


    /**
     * 过滤器类型
     * pre：  在请求被路由（转发）之前调用
     * route：在路由（请求）转发时被调用
     * error：服务网关发生异常时被调用
     * post： 在路由（转发）请求后调用
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;//在请求被路由（转发）之前调用
    }

    /**
     * 优先级为0，数字越大，优先级越低
     * @return
     */
    @Override
    public int filterOrder() {
        return 2;
    }

    /**
     * 是否执行该过滤器
     * @return true 执行 false不执行
     */
    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //约定一下规则的url不需要验证token  也就是不需要登录也能访问的接口 ( 系统定义url约定规则  url保护/external/ )
        boolean falg = PatternMatchUtils.simpleMatch("*/external/*", request.getRequestURI());//包含该规则的返回true
        if(falg){
            return false;
        }
        return true;

    }


    @Override
    public Object run()throws ZuulException{
        try {

            RequestContext requestContext = RequestContext.getCurrentContext();
            HttpServletRequest request = requestContext.getRequest();
            HttpServletResponse response = requestContext.getResponse();
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            SimpleResponse resp=new SimpleResponse();

            String token=request.getHeader("token");
            if(ValidationUtils.isStrsNull(token)){
                writerMessage(response, HttpCodeE.token为空.value, SysRespStatusE.失败.getDesc(),"token为空");
                requestContext.setSendZuulResponse(false);
            }else {
                if(!TokenUtil.verifyToken(token)){
                    writerMessage(response, HttpCodeE.token有误.value, SysRespStatusE.失败.getDesc(),"token有误或者过期");
                    requestContext.setSendZuulResponse(false);
                }else {
                    String userId = TokenUtil.getField(token, "userId");
                    String userTokenKey =null;

                    //包含该规则的返回true (系统定义url预定规则) 包含*/admin/* 表示所有管理平台用户url    不包含就是app用户url  获取对应的redis缓存 userTokenKey
                    boolean falg = PatternMatchUtils.simpleMatch("*/admin/*", request.getRequestURI());
                    if (falg){
                        userTokenKey=RedisCacheKeys.USER_ADMIN_TOKEN_KEY;
                    }else {
                        userTokenKey=RedisCacheKeys.USER_APP_TOKEN_KEY;
                    }
                    String redisToken = cacheStr.getCached(userTokenKey + userId);
                    if(StringUtils.isBlank(redisToken)){
                        writerMessage(response, HttpCodeE.登录已经过期请重新登录.value, SysRespStatusE.失败.getDesc(),"登录已经过期请重新登录");
                        requestContext.setSendZuulResponse(false);
                    }else if (!token.equals(redisToken)){
                        writerMessage(response, HttpCodeE.该账户在其它终端登录.value, SysRespStatusE.失败.getDesc(),"该账户在其它终端登录，如非本人操作请及时修改密码");
                        requestContext.setSendZuulResponse(false);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void writerMessage(HttpServletResponse response,int code, String status, String message)throws Exception {
        SimpleResponse resp=new SimpleResponse();
        PrintWriter pw=response.getWriter();
        resp.setCode(code);
        resp.setStatus(status);
        resp.setMessage(message);
        pw.print(JSON.toJSONString(resp));
        pw.flush();
        pw.close();

    }

}
