package com.cloud.common.response;



import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class SimpleResponse implements Serializable{

    
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="请求状态  默认是success")
	private String status= SysRespStatusE.成功.getDesc();
	
	@ApiModelProperty(value="返回码 默认是200")
	private int code= HttpCodeE.调用成功.value;
	
	@ApiModelProperty(value="返回消息")
	private String message="调用成功！";
	
	@ApiModelProperty(value="扩展字段")
	private Map<String, Object> extData=new HashMap<>();



	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getExtData() {
		return extData;
	}

	public void setExtData(Map<String, Object> extData) {
		this.extData = extData;
	}
	
	/**
	 * 设置返回错误消息
	 * @param resp 返回对象
	 * @param code 错误码
	 * @param status 状态
	 * @param message 错误消息
	 * @return
	 */
	public SimpleResponse setReturnErrMsg(SimpleResponse resp,Integer code,String status,String message){
		resp.setCode(code);
		resp.setStatus(status);
		resp.setMessage(message);
		return resp;
	}


}
