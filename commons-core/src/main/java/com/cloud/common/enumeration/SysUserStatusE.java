package com.cloud.common.enumeration;

public enum SysUserStatusE {


	有效(1),
	无效(0);
	
	
	public int value;
	
	private SysUserStatusE(int value) {
		this.value=value;
	}

}
