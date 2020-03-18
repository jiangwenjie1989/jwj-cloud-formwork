package com.cloud.message.controller;


import com.cloud.common.constants.SwggerCommonTags;
import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.SimpleResponse;
import com.cloud.message.service.ChatMsgService;
import com.cloud.message.vo.ChatMsgVO;
import com.cloud.model.message.ChatMsgDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName : AppUserController  //类名
 * @Description : 用户控制层 //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-10 13:46  //时间
 */
@RequestMapping(value = "/api")
@RestController
public class ChatMsgController {

    @Autowired
    private ChatMsgService chatMsgService;

    @ApiOperation(value = "用户手机端获取未签收的消息列表接口", httpMethod = "GET",tags= SwggerCommonTags.APP_MESSAGE_MODULE)
    @RequestMapping(value = "/queryUnReadMsgList.do",method= RequestMethod.GET)
    public ApiResponse<ChatMsgVO> queryUnReadMsgList() throws Exception {
        return chatMsgService.queryUnReadMsgList();
    }

    @ApiOperation(value = "保存消息记录接口", httpMethod = "POST",tags= SwggerCommonTags.APP_MESSAGE_MODULE)
    @RequestMapping(value = "/internal/saveChatMsg.do",method= RequestMethod.POST)
    public SimpleResponse saveChatMsg(@RequestBody ChatMsgDTO chatMsgDTO) throws Exception {
        return chatMsgService.saveChatMsg(chatMsgDTO);
    }

    @ApiOperation(value = "批量修改消息签收状态接口", httpMethod = "POST",tags= SwggerCommonTags.APP_MESSAGE_MODULE)
    @RequestMapping(value = "/internal/updateChatMsg.do",method= RequestMethod.POST)
    public SimpleResponse updateChatMsg(@RequestParam("msgIdsStr") String msgIdsStr)throws Exception {
        return chatMsgService.updateChatMsg(msgIdsStr);
    }


}
