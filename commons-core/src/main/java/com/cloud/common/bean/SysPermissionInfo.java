package com.cloud.common.bean;

import lombok.Data;


@Data
public class SysPermissionInfo implements java.io.Serializable{

    private Long id;

    private String permission;

    private String name;


}
