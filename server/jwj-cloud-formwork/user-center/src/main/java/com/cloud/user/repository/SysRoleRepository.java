package com.cloud.user.repository;

import com.cloud.model.user.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface SysRoleRepository extends JpaRepository<SysRole, Long> {


    @Query(value="SELECT * FROM sys_role r WHERE r.id IN ( ?1 ) ", nativeQuery=true)
    List<SysRole> findBySysUserIdList(List<Long> sysRoleIdList);

    Optional<SysRole> findByCode(String code);


    @Modifying
    @Query(value="DELETE FROM sys_role  WHERE id IN (?1) " , nativeQuery=true)
    void deleteByIdList(List<Long> idList);
}

