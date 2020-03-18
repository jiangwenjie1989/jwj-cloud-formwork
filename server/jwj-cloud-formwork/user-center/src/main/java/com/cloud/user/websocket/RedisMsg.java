package com.cloud.user.websocket;



/**
 * @ClassName : RedisMsg  //类名
 * @Description : 订阅redis接口  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-10 22:42  //时间
 */
public interface RedisMsg {
    /**
     * 接受信息
     * @param redisMassgae
     */
    public void receiveMessage(String redisMassgae);


}
