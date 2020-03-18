package com.cloud.user.dao;


import com.cloud.model.user.SysUserRole;
import com.cloud.user.repository.SysUserRepository;
import com.cloud.user.repository.SysUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class SysUserRoleDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;

    public List<SysUserRole> findBySysUserId(Long sysUserId) {
        return sysUserRoleRepository.findBySysUserId(sysUserId);
    }

    public void deleteBySysRoleId(ArrayList<Long> idList) {
        sysUserRoleRepository.deleteBySysRoleId(idList);
    }

    public void deleteBySysUserId(Long userId) {
        sysUserRoleRepository.deleteBySysUserId(userId);
    }

    public void saveAll(List<SysUserRole> sysUserRoleList) {
        sysUserRoleRepository.saveAll(sysUserRoleList);
    }
}
