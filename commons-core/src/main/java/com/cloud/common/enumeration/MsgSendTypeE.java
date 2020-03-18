package com.cloud.common.enumeration;

/**
 * 发送消息的类型 枚举
 */
public enum MsgSendTypeE {

	表示内部消息(0),
	表示点对点发送(1),
	表示群发(2),
	表示广播(3);


	public int value;

	private MsgSendTypeE(int value) {
		this.value=value;
	}

}
