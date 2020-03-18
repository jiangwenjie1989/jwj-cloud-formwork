package com.cloud.user.service.impl;

import com.cloud.common.annotation.MsgActionAnnotation;
import com.cloud.common.bean.ChatMessage;
import com.cloud.common.bean.DataContent;
import com.cloud.common.enumeration.MsgActionE;
import com.cloud.common.redis.RedisStringCacheSupport;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.model.message.ChatMsgDTO;
import com.cloud.user.feign.MessageClient;
import com.cloud.user.service.MsgActionService;
import com.cloud.user.websocket.AppUserWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @ClassName : ChatMsgActionServiceImpl  //类名
 * @Description : 聊天消息处理  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-17 10:20  //时间
 */
@Service
@MsgActionAnnotation(msgActionType= MsgActionE.聊天消息)
public class ChatMsgActionServiceImpl implements MsgActionService {

    @Autowired
    private MessageClient messageClient;

    @Autowired
    private AppUserWebSocket appUserWebSocket;



    @Override
    public void doMsgAction(Integer action, DataContent dataContent) {
        ChatMessage chatMsg = dataContent.getChatMsg();
        String msgText = chatMsg.getMsg();
        System.out.println("收到消息"+msgText);
        Long receiverId = chatMsg.getReceiverId();

        //调用消息服务保存消息到数据库
        ChatMsgDTO chatMsgDTO = createChatMsgDTO(dataContent, chatMsg, msgText, receiverId);
        SimpleResponse simpleResponse = messageClient.saveChatMsg(chatMsgDTO);
        if(SysRespStatusE.成功.getDesc().equals(simpleResponse.getStatus())){
            String msgId = simpleResponse.getExtData().get("msgId").toString();
            chatMsg.setMsgId(msgId);
            //发送消息到redis
            appUserWebSocket.sendMessage(dataContent);
        }
    }

    private ChatMsgDTO createChatMsgDTO(DataContent dataContent, ChatMessage chatMsg, String msgText, Long receiverId) {
        ChatMsgDTO chatMsgDTO = new ChatMsgDTO();
        chatMsgDTO.setSendUserId(chatMsg.getSenderId());
        chatMsgDTO.setAcceptUserId(receiverId);
        chatMsgDTO.setContent(msgText);
        chatMsgDTO.setSendType(dataContent.getSendType());
        chatMsgDTO.setMsgType(chatMsg.getMsgType());
        return chatMsgDTO;
    }
}
