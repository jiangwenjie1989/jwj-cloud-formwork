package com.cloud.user.service.impl;

import com.cloud.common.annotation.MsgActionAnnotation;
import com.cloud.common.bean.DataContent;
import com.cloud.common.enumeration.MsgActionE;
import com.cloud.common.utils.ThreadPoolServiceUtils;
import com.cloud.common.utils.ValidationUtils;
import com.cloud.user.feign.MessageClient;
import com.cloud.user.service.MsgActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @ClassName : ChatMsgActionServiceImpl  //类名
 * @Description : 签收动作处理  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-17 10:20  //时间
 */
@Service
@MsgActionAnnotation(msgActionType= MsgActionE.消息签收)
public class SignMsgActionServiceImpl implements MsgActionService {

    @Autowired
    private MessageClient messageClient;

    @Override
    public void doMsgAction(Integer action, DataContent dataContent) {
        // 扩展字段在action=3类型的消息中，代表需要去签收的消息id，逗号间隔
        String msgIdsStr = dataContent.getExtand();
        if(!ValidationUtils.StrisNull(msgIdsStr)){
            //异步调用消息服务批量签收消息
            CompletableFuture.runAsync(() -> {
                messageClient.updateChatMsg(msgIdsStr);
            }, ThreadPoolServiceUtils.getInstance());
        }
    }



}
