package com.cloud.admin.vo.menu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.sql.Timestamp;

/**
 * @ClassName : MenuVO  //类名
 * @Description : 菜单VO  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 17:52  //时间
 */
@Data
public class MenuVO {

    @ApiModelProperty(value="菜单id")
    private Long id;

    @ApiModelProperty(value="父菜单id")
    private Long parentId;

    @ApiModelProperty(value="菜单名称")
    private String name;

    @ApiModelProperty(value="菜单url")
    private String url;

    @ApiModelProperty(value="菜单样式")
    private String css;

    @ApiModelProperty(value="排序")
    private Integer sort;

    @ApiModelProperty(value="创建时间")
    private Timestamp createTime;

}
