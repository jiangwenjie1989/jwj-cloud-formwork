package com.cloud.user.repository;

import com.cloud.model.user.SysRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;


public interface SysRolePermissionRepository extends JpaRepository<SysRolePermission, Long> {


    @Query(value="SELECT * FROM sys_role_permission p WHERE p.sys_role_id IN (?1) ", nativeQuery=true)
    List<SysRolePermission> findBySysRoleIdList(List<Long> sysRoleIdList);

    @Modifying
    @Query(value="DELETE FROM sys_role_permission  WHERE sys_role_id IN (?1) ", nativeQuery=true)
    void deleteBySysRoleId(List<Long> ids);

    @Modifying
    @Query(value="DELETE FROM sys_role_permission  WHERE sys_permission_id IN (?1) ", nativeQuery=true)
    void findBySysPermissionIdList(ArrayList<Long> permissionIdList);
}
