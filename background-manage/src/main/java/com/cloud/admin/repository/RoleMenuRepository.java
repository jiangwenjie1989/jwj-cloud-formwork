package com.cloud.admin.repository;

import com.cloud.model.manage.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {

    @Modifying
    @Query(value="DELETE FROM role_menu  WHERE sys_role_id = (?1) ", nativeQuery=true)
    void deleteByRoleIds(List<Long> roleIds);

    @Modifying
    @Query(value="DELETE FROM role_menu  WHERE menu_id = (?1) ", nativeQuery=true)
    void deleteByMenuIds(List<Long> menuIdList);

    @Query(value="SELECT * FROM role_menu  WHERE sys_role_id = (?1) ", nativeQuery=true)
    List<RoleMenu> findBySysRoleIds(List<Long> sysRoleIdList);
}
