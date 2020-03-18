package com.cloud.message.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.sql.Timestamp;

/**
 * @ClassName : ChatMsgVO  //类名
 * @Description : 聊天消息记录Vo  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-11 08:48  //时间
 */
@Data
public class ChatMsgVO {

    @ApiModelProperty(value="消息id")
    private Long id;

    @ApiModelProperty(value="发送方id")
    private Long sendUserId;
    /** 接收方id */
    @ApiModelProperty(value="接收方id")
    private Long acceptUserId;

    @ApiModelProperty(value="消息id")
    private String content;

    @ApiModelProperty(value="消息类型 1文字 2语音 3图片等等")
    private Integer msgType;

    @ApiModelProperty(value="创建时间")
    private Timestamp createTime;

}
