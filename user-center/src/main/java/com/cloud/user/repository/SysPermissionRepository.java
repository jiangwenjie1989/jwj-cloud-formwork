package com.cloud.user.repository;

import com.cloud.model.user.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public interface SysPermissionRepository extends JpaRepository<SysPermission, Long> {

    @Query(value="SELECT * FROM sys_permission p WHERE p.id IN (?1) ", nativeQuery=true)
    List<SysPermission> findByPermissionIdList(List<Long> sysPermissionIdList);

    Optional<SysPermission> findByPermission(String permission);

    @Modifying
    @Query(value="DELETE FROM sys_permission  WHERE id IN (?1) ", nativeQuery=true)
    void deleteByIds(ArrayList<Long> idList);
}
