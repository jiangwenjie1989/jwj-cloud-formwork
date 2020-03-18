package com.cloud.model.manage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName : BlackIpDTO  //类名
 * @Description : ip黑名单dto  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-08 18:22  //时间
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlackIpDTO implements Serializable {

    private String ipAddress;

}
