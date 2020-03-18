package com.cloud.user.vo.permission;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;


@Data
public class SysPermissionInfoVO implements java.io.Serializable{

    @ApiModelProperty(value="权限id")
    private Long id;

    @ApiModelProperty(value="权限标识")
    private String permission;

    @ApiModelProperty(value="权限名称")
    private String name;

    @ApiModelProperty(value="创建时间")
    private Timestamp createTime;


}
