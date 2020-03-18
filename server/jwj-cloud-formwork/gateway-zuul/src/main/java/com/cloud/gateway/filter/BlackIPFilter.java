package com.cloud.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.cloud.common.constants.MessageQueue;
import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.gateway.feign.ManageClient;
import com.cloud.model.manage.BlackIpDTO;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName : BlackIPFilter  //类名
 * @Description : ip黑名单过滤器  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-04 12:23  //时间
 */
@Slf4j
@Component
@RabbitListener(queues = MessageQueue.BLACK_IP_QUEUE)
public class BlackIPFilter extends ZuulFilter {

    @Autowired
    private ManageClient manageClient;

    /**
     * 黑名单列表
     */
    private Set<String> blackIPs = new HashSet<>();



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
        return 0;
    }

    /**
     * 是否执行该过滤器，为true，说明需要过滤  false不需要执行该过滤器
     * @return
     */
    @Override
    public boolean shouldFilter() {
        if (blackIPs.isEmpty()) {
            return false;
        }else {
            RequestContext requestContext = RequestContext.getCurrentContext();
            HttpServletRequest request = requestContext.getRequest();
            String ip = getIpAddress(request);
            boolean flag = blackIPs.contains(ip);// 判断ip是否在黑名单列表里
            if (flag) {
                log.info("客户端ip黑名单:{}", ip);
            }
            return flag;
        }
    }

    @Override
    public Object run() throws ZuulException {
        try {
            RequestContext requestContext = RequestContext.getCurrentContext();
            HttpServletRequest request = requestContext.getRequest();
            HttpServletResponse response = requestContext.getResponse();
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            writerMessage(response, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(),"该ip在黑名单存在");
            requestContext.setSendZuulResponse(false);
        } catch (Exception e) {
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


    /**
     * 保存ip到黑名单集合
     */
    @RabbitHandler
    public void syncBlackIPList(String blackIP) {
        ApiResponse<BlackIpDTO> blackIpDTOApiResponse = manageClient.selectAllBlackIp();
        if("success".equals(blackIpDTOApiResponse.getStatus())){
            List<BlackIpDTO> blackIpDTOlist = blackIpDTOApiResponse.getList();
            if(!CollectionUtils.isEmpty(blackIpDTOlist)){
                blackIPs = new HashSet<>();
                for (BlackIpDTO blackIpDTO : blackIpDTOlist) {
                    blackIPs.add(blackIpDTO.getIpAddress());
                }
            }
        }
    }

    /**
     * 获取请求的真实ip
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}
