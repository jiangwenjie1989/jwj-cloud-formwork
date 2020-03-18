package com.cloud.common.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息对象
 */
@Data
public class ChatMessage implements Serializable {

	private static final long serialVersionUID = 3611169682695799175L;

	@ApiModelProperty(value = "发送者的用户id")
	private Long senderId;

	@ApiModelProperty(value = "接受者的用户id")
	private Long receiverId;

	@ApiModelProperty(value = "聊天内容")
	private String msg;

	@ApiModelProperty(value = "用于消息的签收 消息id")
	private String msgId;

	@ApiModelProperty(value = "消息类型 1文字 2语音 3图片 默认值1")
	private Integer msgType=1;
	
}
