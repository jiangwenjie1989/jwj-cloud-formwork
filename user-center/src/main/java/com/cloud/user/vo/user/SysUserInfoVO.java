package com.cloud.user.vo.user;

import com.cloud.user.vo.permission.SysPermissionInfoVO;
import com.cloud.user.vo.role.SysRoleInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : SysUserInfoVO  //类名
 * @Description : 管理系统用户信息vo  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-03 14:47  //时间
 */
@Data
public class SysUserInfoVO implements java.io.Serializable{

    @ApiModelProperty(value="用户token")
    private String token="";

    @ApiModelProperty(value="用户名称")
    private String username;

    @ApiModelProperty(value="手机号")
    private String phone;

    @ApiModelProperty(value="昵称")
    private String nickname;

    @ApiModelProperty(value="头像url")
    private String headImgUrl;

    @ApiModelProperty(value="状态 1表示有效 0 表示无效")
    private Integer status;

    @ApiModelProperty(value="角色集合")
    List<SysRoleInfoVO> sysRoleList=new ArrayList<>();

    @ApiModelProperty(value="权限集合")
    List<SysPermissionInfoVO> sysPermissionList=new ArrayList<>();

}
