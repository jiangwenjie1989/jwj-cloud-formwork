package com.cloud.user.dao;

import com.cloud.model.user.SysRolePermission;
import com.cloud.user.repository.SysPermissionRepository;
import com.cloud.user.repository.SysRolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class SysRolePermissionDao {

    @Autowired
    private SysRolePermissionRepository sysRolePermissionRepository;


    public List<SysRolePermission> findBySysRoleIdList(List<Long> sysRoleIdList) {
        return sysRolePermissionRepository.findBySysRoleIdList(sysRoleIdList);
    }

    public void deleteBySysRoleId(List<Long> ids) {
        sysRolePermissionRepository.deleteBySysRoleId(ids);
    }

    public void saveAll(List<SysRolePermission> sysRolePermissionList) {
        sysRolePermissionRepository.saveAll(sysRolePermissionList);
    }

    public void findBySysPermissionIdList(ArrayList<Long> idList) {
        sysRolePermissionRepository.findBySysPermissionIdList(idList);
    }
}
