package com.cloud.task.handle;

import com.cloud.common.utils.IPV4Util;
import com.cloud.task.config.JobServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @ClassName : DemoTask  //类名
 * @Description : 例子定时任务  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-10 12:20  //时间
 */
@Component
public class DemoTask {

    @Autowired
   private JobServiceConfig jobServiceConfig;


    /*
     集群的情况下 保证只有一台实例执行定时任务
     第一步先获取当前服务ip
     第二步获取springcloud集群ip信息
     最后将当前ip和集群的ip进行对比，如果当前ip是集群中最小的ip则执行定时任务业务，如果不是则return掉。
    */


    @Async("taskExecutor")
    //@Scheduled(cron = "0/5 * * * * ?")
    public void doWork() {

        if (!IPV4Util.ipCompare(this.jobServiceConfig.serviceUrl())) {
            return;
        }
        //执行自己的业务逻辑
        System.out.println(IPV4Util.getIpAddress());

    }

}
