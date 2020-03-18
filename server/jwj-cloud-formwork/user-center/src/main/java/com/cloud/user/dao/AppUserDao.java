package com.cloud.user.dao;


import com.cloud.model.user.AppUser;
import com.cloud.user.base.BaseDao;
import com.cloud.user.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * @ClassName : AppUserDao  //类名
 * @Description : app用户Dao  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-10 14:37  //时间
 */
@Repository
public class AppUserDao extends BaseDao {



    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AppUserRepository appUserRepository;


    public Optional<AppUser> findByPhone(String phone) {
        return appUserRepository.findByPhone(phone);
    }

    public AppUser save(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    public Optional<AppUser> findById(Long userId) {
        return appUserRepository.findById(userId);
    }

    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }
}
