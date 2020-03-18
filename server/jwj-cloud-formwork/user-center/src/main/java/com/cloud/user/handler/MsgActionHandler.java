package com.cloud.user.handler;

import com.alibaba.fastjson.JSON;
import com.cloud.common.annotation.MsgActionAnnotation;
import com.cloud.common.bean.DataContent;
import com.cloud.user.service.MsgActionService;
import com.cloud.user.utils.SpringBeanUtils;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * @ClassName : MsgActionHandler  //类名
 * @Description : 消息动作处理类  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-17 09:51  //时间
 */
@Service
public class MsgActionHandler {

    public void process(Integer action, DataContent dataContent) {

        Map<String, MsgActionService> beanMap = SpringBeanUtils.getBeanMap(MsgActionService.class);
        try {
            for (Map.Entry<String, MsgActionService> entry : beanMap.entrySet()) {
                Object real = SpringBeanUtils.getTarget(entry.getValue());
                MsgActionAnnotation annotation = real.getClass().getAnnotation(MsgActionAnnotation.class);
                if (action.equals(annotation.msgActionType().value)) {
                    entry.getValue().doMsgAction(action,dataContent);
                    break;
                }
            }
        } catch (Exception e) {

        }
    }
}
