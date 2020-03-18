package com.cloud.common.bean;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;


@Data
public class SysUserInfo implements java.io.Serializable{


    private String token="";


    private String username;


    private String phone;


    private String nickname;


    private String headImgUrl;


    private Integer status;


    List<SysRoleInfo> sysRoleList=new ArrayList<>();


    List<SysPermissionInfo> sysPermissionList=new ArrayList<>();

}
