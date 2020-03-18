package com.cloud.user.feign.fallback;

import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.model.file.FileDTO;
import com.cloud.model.message.ChatMsgDTO;
import com.cloud.user.feign.FileClient;
import com.cloud.user.feign.MessageClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName : MessageClientFallback  //类名
 * @Description : 失败降级  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 12:42  //时间
 */
@Component
public class MessageClientFallback implements FallbackFactory<MessageClient> {

    @Override
    public MessageClient create(Throwable e) {
        return new MessageClient() {
            @Override
            public SimpleResponse saveChatMsg(ChatMsgDTO chatMsgDTO) {

                SimpleResponse resp = new SimpleResponse();
                return resp.setReturnErrMsg(resp, HttpCodeE.调用本地服务失败.value,
                        SysRespStatusE.失败.getDesc(), "调用本地服务失败！ManageClient.saveChatMsg");
            }

            @Override
            public SimpleResponse updateChatMsg(String msgIdsStr) {
                SimpleResponse resp = new SimpleResponse();
                return resp.setReturnErrMsg(resp, HttpCodeE.调用本地服务失败.value,
                        SysRespStatusE.失败.getDesc(), "调用本地服务失败！ManageClient.updateChatMsg");
            }
        };
    }


}
