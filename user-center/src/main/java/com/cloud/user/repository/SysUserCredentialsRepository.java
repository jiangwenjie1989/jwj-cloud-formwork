package com.cloud.user.repository;

import com.cloud.model.user.SysUserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SysUserCredentialsRepository extends JpaRepository<SysUserCredentials, Long> {

    Optional<SysUserCredentials> findBySysUserIdAndLoginAccount(Long sysUserId, String username);



}
