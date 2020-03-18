package com.cloud.task.config;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : JobServiceConfig  //类名
 * @Description : 任务配置  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-10 12:16  //时间
 */
@Component
public class JobServiceConfig  {

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    private DiscoveryClient discoveryClient;


    public List<URI> serviceUrl() {
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(serviceName);
        List<URI> urlList = new ArrayList<URI>();
        if (CollectionUtils.isNotEmpty(serviceInstanceList)) {
            serviceInstanceList.forEach(si -> {
                urlList.add(si.getUri());
            });
        }
        return urlList;
    }


}
