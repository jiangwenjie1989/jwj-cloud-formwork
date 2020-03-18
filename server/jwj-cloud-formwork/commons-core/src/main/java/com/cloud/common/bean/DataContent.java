package com.cloud.common.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 和客户端消息交互对象
 */
@Data
public class DataContent implements Serializable {

	private static final long serialVersionUID = 8021381444738260454L;

	@ApiModelProperty(value = "动作类型 有个枚举对应")
	private Integer action;

	@ApiModelProperty(value = "用户的聊天内容entity")
	private ChatMessage chatMsg;

	@ApiModelProperty(value = "发送类型 1表示点对点发送 2表示表示群发 3表示广播 默认值")
	private Integer sendType=1;

	@ApiModelProperty(value = "群id 只有当sendType=2 群发的时候才有值")
	private String groupId;

	@ApiModelProperty(value = "扩展字段")
	private String extand;


	

}
