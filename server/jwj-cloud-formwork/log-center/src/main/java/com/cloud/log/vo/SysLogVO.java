package com.cloud.log.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.sql.Timestamp;

/**
 * @ClassName : SysLogVO  //类名
 * @Description : 系统日志vo  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-08 16:42  //时间
 */
@Data
public class SysLogVO {

    @ApiModelProperty(value="用户名称")
    private String username;

    @ApiModelProperty(value="操作名称")
    private String doName;

    @ApiModelProperty(value="方法参数")
    private String params;

    @ApiModelProperty(value="备注")
    private String remark;

    @ApiModelProperty(value="是否成功 1表示成功 0表示失败")
    private Integer flag;

    @ApiModelProperty(value="创建时间")
    private Timestamp createTime;


}
