package com.cloud.admin.vo.blackip;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.sql.Timestamp;

/**
 * @ClassName : BlackIpVO  //类名
 * @Description : 黑名单vo  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-08 17:11  //时间
 */
@Data
public class BlackIpVO {

    @ApiModelProperty(value="黑名单id")
    private Long id;

    @ApiModelProperty(value="ip地址")
    private String ipAddress;

    @ApiModelProperty(value="创建时间")
    private Timestamp createTime;
}
