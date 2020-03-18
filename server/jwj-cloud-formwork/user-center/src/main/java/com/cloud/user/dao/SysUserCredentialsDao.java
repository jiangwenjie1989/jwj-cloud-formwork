package com.cloud.user.dao;


import com.cloud.model.user.SysUserCredentials;
import com.cloud.user.repository.SysUserCredentialsRepository;
import com.cloud.user.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class SysUserCredentialsDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private SysUserCredentialsRepository sysUserCredentialsRepository;

    public void saveAll(List<SysUserCredentials> sysUserCredentialsList) {
        sysUserCredentialsRepository.saveAll(sysUserCredentialsList);
    }



    public void save(SysUserCredentials sysUserCredentials) {
        sysUserCredentialsRepository.save(sysUserCredentials);
    }

    public Optional<SysUserCredentials> findBySysUserIdAndLoginAccount(Long sysUserId, String username) {
        return sysUserCredentialsRepository.findBySysUserIdAndLoginAccount(sysUserId,username);
    }

//    @Autowired
//    private RedisCacheSupport<AppUser> cache;


}
