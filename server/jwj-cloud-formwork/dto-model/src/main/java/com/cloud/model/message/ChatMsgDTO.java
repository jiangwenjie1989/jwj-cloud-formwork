package com.cloud.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsgDTO implements Serializable {

    /** 发送方id */
    private Long sendUserId;

    /** 接收方id */
    private Long acceptUserId;

    /** 消息内容 */
    private String content;

    /** 发送类型 1点对点发送 2消息群发 */
    private Integer sendType;

    /** 消息类型 1文字 2语音 3图片等等 */
    private Integer msgType;


}
