package com.cloud.message.service.impl;

import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.common.utils.DateUntil;
import com.cloud.common.utils.ThreadLocalUtil;
import com.cloud.message.dao.ChatMsgDao;
import com.cloud.message.service.ChatMsgService;
import com.cloud.message.vo.ChatMsgVO;
import com.cloud.model.message.ChatMsg;
import com.cloud.model.message.ChatMsgDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName : ChatMsgServiceImpl  //类名
 * @Description : 聊天消息service  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-11 08:45  //时间
 */
@Service
public class ChatMsgServiceImpl implements ChatMsgService {

    @Autowired
    private ChatMsgDao chatMsgDao;

    @Override
    public ApiResponse<ChatMsgVO> queryUnReadMsgList() {
        ApiResponse<ChatMsgVO> resp=new ApiResponse<>();
        Long userId = ThreadLocalUtil.getUserIdHolder();
        List<ChatMsg> unReadMsgList=chatMsgDao.queryUnReadMsgList(userId);
        if(CollectionUtils.isEmpty(unReadMsgList)){
            return resp.setReturnErrMsg(resp, HttpCodeE.调用成功.value, SysRespStatusE.成功.getDesc(), "没有数据!");
        }
        List<ChatMsgVO> chatMsgVOList = unReadMsgList.stream().map(chatMsg -> {
            ChatMsgVO chatMsgVO = new ChatMsgVO();
            BeanUtils.copyProperties(chatMsg,chatMsgVO);
            return chatMsgVO;
        }).collect(Collectors.toList());
        resp.setList(chatMsgVOList);
        return resp;
    }

    @Transactional
    @Override
    public SimpleResponse saveChatMsg(ChatMsgDTO chatMsgDTO) {
        SimpleResponse resp=new SimpleResponse();
        ChatMsg chatMsg = new ChatMsg();
        BeanUtils.copyProperties(chatMsgDTO,chatMsg);
        chatMsg.setCreateTime(DateUntil.getNowTimestamp());
        ChatMsg chatMsgDB=chatMsgDao.save(chatMsg);
        HashMap<String, Object> hashMap= new HashMap<>();
        hashMap.put("msgId",chatMsgDB.getId());
        resp.setExtData(hashMap);
        return resp;
    }

    @Transactional
    @Override
    public SimpleResponse updateChatMsg(String msgIdsStr) {
        SimpleResponse resp=new SimpleResponse();
        String msgIds[] = msgIdsStr.split(",");
        List<Long> msgIdList = new ArrayList<>();
        for (String mid : msgIds) {
            msgIdList.add(Long.parseLong(mid));
        }
        chatMsgDao.updateByIds(msgIdList);
        return resp;
    }
}
