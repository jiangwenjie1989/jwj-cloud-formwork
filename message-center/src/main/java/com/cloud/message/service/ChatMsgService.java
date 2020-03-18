package com.cloud.message.service;

import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.SimpleResponse;
import com.cloud.message.vo.ChatMsgVO;
import com.cloud.model.message.ChatMsgDTO;

public interface ChatMsgService {
    ApiResponse<ChatMsgVO> queryUnReadMsgList();

    SimpleResponse saveChatMsg(ChatMsgDTO chatMsgDTO);

    SimpleResponse updateChatMsg(String msgIdsStr);
}
