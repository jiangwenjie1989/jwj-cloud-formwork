package com.cloud.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisMassgae {

    //频道
    private String channel;

    //发送内容
    private DataContent dataContent;

}
