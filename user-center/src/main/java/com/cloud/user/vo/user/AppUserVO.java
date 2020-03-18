package com.cloud.user.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName : AppUserVO  //类名
 * @Description : app用户VO  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-10 16:05  //时间
 */
@Data
public class AppUserVO {
    @ApiModelProperty(value="用户id")
    private Long id;

    @ApiModelProperty(value="手机号")
    private String phone;

    @ApiModelProperty(value="用户名称")
    private String username;

    @ApiModelProperty(value="头像小图url")
    private String faceImage;

    @ApiModelProperty(value="头像原图url")
    private String faceImageBig;

    @ApiModelProperty(value="昵称")
    private String nickname;

    @ApiModelProperty(value="二维码")
    private String qrcode;

    @ApiModelProperty(value="用户token")
    private String token;


}
