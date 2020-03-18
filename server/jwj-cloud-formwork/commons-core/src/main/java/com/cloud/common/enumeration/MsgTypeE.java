package com.cloud.common.enumeration;

/**
 * 消息类型 枚举
 */
public enum MsgTypeE {



	文本(1),
	语音(2),
	图片等等(3);


	public int value;

	private MsgTypeE(int value) {
		this.value=value;
	}

}
