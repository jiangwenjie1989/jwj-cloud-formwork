package com.cloud.user.repository;

import com.cloud.model.user.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {


    Optional<UserFriend> findByUserIdAndFriendUserId(Long userId, Long friendId);

}
