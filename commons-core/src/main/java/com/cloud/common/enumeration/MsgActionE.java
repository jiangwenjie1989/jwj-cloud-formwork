package com.cloud.common.enumeration;

/**
 * 发送消息的动作 枚举
 */
public enum MsgActionE {

	第一次或重连初始化连接(1),
	聊天消息(2),
	消息签收(3),
	客户端保持心跳(4),
	拉取好友(5),
	请求添加好友(6);


	public int value;

	private MsgActionE(int value) {
		this.value=value;
	}

	public int getValue() {
		return value;
	}

}
