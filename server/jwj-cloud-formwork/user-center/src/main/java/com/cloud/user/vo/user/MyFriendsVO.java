package com.cloud.user.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MyFriendsVO {

	@ApiModelProperty(value="好友用户名称")
    private String friendUserId;

	@ApiModelProperty(value="好友用户名称")
    private String friendUsername;

	@ApiModelProperty(value="好友头像")
    private String friendFaceImage;

	@ApiModelProperty(value="好友用户名称")
    private String friendNickname;
    

    
}