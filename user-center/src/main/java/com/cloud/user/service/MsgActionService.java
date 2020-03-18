package com.cloud.user.service;

import com.cloud.common.bean.DataContent;

public interface MsgActionService {

    abstract public void doMsgAction(Integer action, DataContent dataContent);
}
