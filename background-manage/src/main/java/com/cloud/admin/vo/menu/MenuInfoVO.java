package com.cloud.admin.vo.menu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @ClassName : MenuVO  //类名
 * @Description : 菜单VO  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 17:52  //时间
 */
@Data
public class MenuInfoVO {

    @ApiModelProperty(value="菜单名称")
    private String name;

    @ApiModelProperty(value="菜单url")
    private String url;

    @ApiModelProperty(value="菜单样式")
    private String css;

}
