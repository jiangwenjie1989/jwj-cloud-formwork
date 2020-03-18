package com.cloud.user.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SysUserInfoListVO implements java.io.Serializable{

    @ApiModelProperty(value="用户id")
    private Long userId;

    @ApiModelProperty(value="用户名称")
    private String username;

    @ApiModelProperty(value="昵称")
    private String nickname;

    @ApiModelProperty(value="手机号")
    private String phone;

    @ApiModelProperty(value="状态 1表示有效 0 表示无效 ")
    private Integer status;

    @ApiModelProperty(value="头像")
    private String headImgUrl;

    @ApiModelProperty(value="创建时间")
    private String createTime;

}
