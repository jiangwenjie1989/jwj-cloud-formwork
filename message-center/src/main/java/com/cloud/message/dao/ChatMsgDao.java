package com.cloud.message.dao;



import com.cloud.common.enumeration.MsgSendTypeE;
import com.cloud.common.enumeration.MsgSignFlagE;
import com.cloud.common.enumeration.MsgTypeE;
import com.cloud.message.base.BaseDao;
import com.cloud.message.repository.ChatMsgRepository;
import com.cloud.message.vo.ChatMsgVO;
import com.cloud.model.message.ChatMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * @ClassName : ChatMsgDao  //类名
 * @Description : 聊天消息dao  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-11 08:37  //时间
 */
@Repository
public class ChatMsgDao extends BaseDao {



    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ChatMsgRepository chatMsgRepository;


    public List<ChatMsg> queryUnReadMsgList(Long userId) {

        return chatMsgRepository.findByAcceptUserIdAndSendTypeAndMsgTypeAndSignFlag(userId,
                MsgSendTypeE.表示点对点发送.value, MsgTypeE.文本.value, MsgSignFlagE.没有签收.value);
    }

    public ChatMsg save(ChatMsg chatMsg) {
        return chatMsgRepository.save(chatMsg);
    }

    public void updateByIds(List<Long> msgIdList) {
        chatMsgRepository.updateByIds(msgIdList);
    }
}
