package com.cloud.user.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 好友请求发送方的信息
 */
@Data
public class FriendRequestVO {

	@ApiModelProperty(value="发送方用户id")
    private long sendUserId;

	@ApiModelProperty(value="发送方用户名称")
    private String sendUsername;

	@ApiModelProperty(value="发送方头像url")
    private String sendFaceImage;

	@ApiModelProperty(value="发送方昵称")
    private String sendNickname;
    

}