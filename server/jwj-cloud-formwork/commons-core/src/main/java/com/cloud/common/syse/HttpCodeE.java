package com.cloud.common.syse;

public enum HttpCodeE {

	调用成功(200),
	没有数据(400),
	重复提交(401),
	该账户在其它终端登录(402),
	参数有误(403),
	登录已经过期请重新登录(413),
	内部错误(500),
	数据验证不通过(501),
	缺少认证参数(502),
	调用频繁(503),
	权限不足(504),
	调用本地服务失败(505),
	token为空(506),
	token有误(507),
	该链接已经过期(600),
	签名验证不通过(601);
	
	
	public int value;
	
	private HttpCodeE(int value) {
		this.value=value;
	}

}
