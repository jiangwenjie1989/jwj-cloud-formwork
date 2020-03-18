package com.cloud.common.enumeration;

public enum SysUserLoginTypeE {


	用户名登录(1),
	手机号登录 (2),
	微信登录(3),
	支付宝登录(4);


	public int value;

	private SysUserLoginTypeE(int value) {
		this.value=value;
	}

}
