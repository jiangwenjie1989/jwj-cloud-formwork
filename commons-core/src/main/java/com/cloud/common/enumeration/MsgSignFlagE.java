package com.cloud.common.enumeration;

/**
 * 签收 枚举
 */
public enum MsgSignFlagE {



	没有签收(0),
	签收(1);

	public int value;

	private MsgSignFlagE(int value) {
		this.value=value;
	}

}
