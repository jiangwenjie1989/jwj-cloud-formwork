package com.cloud.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.common.utils.ApiSign;
import com.cloud.common.utils.RSAUtil;
import com.cloud.common.utils.ValidationUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @ClassName : SecurityParmFilter  //类名
 * @Description : 参数安全过滤器  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-03 16:07  //时间
 */
@Component
public class SecurityParmFilter extends ZuulFilter {

    @Value("${app.privateKey}")
    private String privateKey;

    @Value("${app.salt}")
    private String salt;


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
        return 1;
    }

    /**
     * 过滤器是否执行 true表示执行 false不执行
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return false;
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


            String timestamp=request.getHeader("timestamp");
            String requestId=request.getHeader("requestId");
            String channelType=request.getHeader("channelType");
            String sign=request.getHeader("sign");
            if(ValidationUtils.isStrsNull(timestamp,requestId,channelType,sign)){
                writerMessage(response, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(),"参数传入为空");
                requestContext.setSendZuulResponse(false);
            }else {
                Map<String, String[]> reqParas=request.getParameterMap();
                String mysign= ApiSign.getSignature(reqParas,channelType,salt,requestId);
                //System.out.println("客户端签名sign="+sign);
                //System.out.println("服务端签名mysign="+mysign);
                //服务端解密
                String encryptSt = RSAUtil.encryptByprivateKey(sign,privateKey, Cipher.DECRYPT_MODE);
                //System.out.println("服务端解密mysign="+encryptSt);
                if(!mysign.equals(encryptSt)){
                    writerMessage(response, HttpCodeE.签名验证不通过.value, SysRespStatusE.失败.getDesc(),"签名验证不通过");
                    requestContext.setSendZuulResponse(false);
                }
            }

        }catch (Exception e){
            //e.printStackTrace();
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
