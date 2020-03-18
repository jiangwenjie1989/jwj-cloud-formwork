package com.cloud.user.repository;

import com.cloud.model.user.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {


    Optional<FriendRequest> findBySendUserIdAndAcceptUserId(Long userId, Long friendId);

    @Modifying
    @Query(value="DELETE FROM friend_request  WHERE send_user_id = ?1 and accept_user_id = ?2  " , nativeQuery=true)
    void deleteBySendUserIdAndAcceptUserId(Long sendUserId, Long userId);
}
