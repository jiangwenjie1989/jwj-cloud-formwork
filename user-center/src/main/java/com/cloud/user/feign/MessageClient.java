package com.cloud.user.feign;

import com.cloud.common.response.SimpleResponse;
import com.cloud.model.message.ChatMsgDTO;
import com.cloud.user.feign.fallback.MessageClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "message-center",fallbackFactory = MessageClientFallback.class)
public interface MessageClient {

    @PostMapping(value = "/api/internal/saveChatMsg.do")
    public SimpleResponse saveChatMsg(@RequestBody ChatMsgDTO chatMsgDTO);

    @PostMapping(value = "/api/internal/updateChatMsg.do")
    public SimpleResponse updateChatMsg(@RequestParam("msgIdsStr") String msgIdsStr);

}
