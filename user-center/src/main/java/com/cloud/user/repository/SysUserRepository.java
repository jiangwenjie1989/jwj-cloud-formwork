package com.cloud.user.repository;

import com.cloud.model.user.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface SysUserRepository extends JpaRepository<SysUser, Long> {


    Optional<SysUser> findByPhone(String phone);

    Optional<SysUser> findByUsername(String username);

    @Query(value="SELECT u.* FROM sys_user u INNER JOIN sys_user_credentials c ON u.id=c.sys_user_id WHERE c.login_account=?1 ", nativeQuery=true)
    Optional<SysUser> findByLoginAccount(String loginAccount);
}
