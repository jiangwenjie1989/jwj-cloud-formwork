package com.cloud.message.repository;

import com.cloud.message.vo.ChatMsgVO;
import com.cloud.model.message.ChatMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ChatMsgRepository extends JpaRepository<ChatMsg, Long> {


    List<ChatMsg> findByAcceptUserIdAndSendTypeAndMsgTypeAndSignFlag(Long acceptUserId, int sendType, int msgType, int signFlag);

    @Modifying
    @Query(value="UPDATE chat_msg SET sign_flag=1  WHERE id IN ( ?1 ) ", nativeQuery=true)
    void updateByIds(List<Long> msgIdList);
}
