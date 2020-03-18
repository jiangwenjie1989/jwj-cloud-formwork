package com.cloud.user.vo.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.sql.Timestamp;

/**
 * @ClassName : SysRoleInfoVO  //类名
 * @Description : 管理角色vo  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-03 14:47  //时间
 */
@Data
public class SysRoleInfoVO implements java.io.Serializable{

    @ApiModelProperty(value="角色id")
    private Long id;

    @ApiModelProperty(value="角色code")
    private String code;

    @ApiModelProperty(value="角色名称")
    private String name;

    @ApiModelProperty(value="创建时间")
    private Timestamp createTime;

}
