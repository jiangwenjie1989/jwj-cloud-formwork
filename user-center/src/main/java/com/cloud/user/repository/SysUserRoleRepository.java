package com.cloud.user.repository;

import com.cloud.model.user.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;


public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {


    List<SysUserRole> findBySysUserId(Long sysUserId);


    @Modifying
    @Query(value="DELETE FROM sys_user_role  WHERE sys_role_id IN (?1) ", nativeQuery=true)
    void deleteBySysRoleId(ArrayList<Long> idList);

    @Modifying
    @Query(value="DELETE FROM sys_user_role  WHERE sys_userId_id = (?1) ", nativeQuery=true)
    void deleteBySysUserId(Long userId);
}
