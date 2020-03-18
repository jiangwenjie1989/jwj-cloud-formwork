package com.cloud.user.websocket;

import com.alibaba.fastjson.JSON;
import com.cloud.common.bean.ChatMessage;
import com.cloud.common.bean.DataContent;
import com.cloud.common.bean.RedisMassgae;
import com.cloud.common.enumeration.MsgSendTypeE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName : ChannelRedisListener  //类名
 * @Description :   订阅redis实现 //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-10 22:42  //时间
 */
@Component
public class ChannelRedisListener implements RedisMsg {

    @Value("${channel.im.name}")
    private  String imChannel;


    @Autowired
    private AppUserWebSocket appUserWebSocket;

    @Override
    public void receiveMessage(String massgae){
        RedisMassgae redisMassgae = JSON.parseObject(massgae, RedisMassgae.class);
        if(redisMassgae.getChannel().equals(imChannel)){

            DataContent dataContent = redisMassgae.getDataContent();

            Integer sendType = dataContent.getSendType();
            ChatMessage chatMsg = dataContent.getChatMsg();

            if(sendType.equals(MsgSendTypeE.表示点对点发送.value)){

                appUserWebSocket.sendMessageToUser(JSON.toJSONString(dataContent),chatMsg.getReceiverId());

            }else if(sendType.equals(MsgSendTypeE.表示广播.value)){

                appUserWebSocket.broadcast(JSON.toJSONString(chatMsg));

            }else if(sendType.equals(MsgSendTypeE.表示群发.value)){

                // TODO 以后实现

            }
        }
    }
}
