package com.cloud.user.dao;


import com.cloud.common.response.Pagetool;
import com.cloud.common.utils.ValidationUtils;
import com.cloud.model.user.SysUser;
import com.cloud.user.base.BaseDao;
import com.cloud.user.repository.SysUserRepository;
import com.cloud.user.vo.user.SysUserInfoListVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


/**
 * @ClassName : SysUserDao  //类名
 * @Description : 用户Dao  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-03 14:37  //时间
 */
@Repository
public class SysUserDao extends BaseDao {

    //    @Autowired
//    private RedisCacheSupport<AppUser> cache;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SysUserRepository sysUserRepository;

    public Optional<SysUser> findByPhone(String phone) {
        return sysUserRepository.findByPhone(phone);
    }

    public Optional<SysUser> findByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }

    public SysUser save(SysUser sysUser) {
        return sysUserRepository.save(sysUser);
    }

    public Optional<SysUser> findById(Long userId) {
        return sysUserRepository.findById(userId);
    }

    public Pagetool<SysUserInfoListVO> querySysUserPage(Integer currentPage, Integer pageSize, String phone, String username, String nickname, Integer status) {
        Pagetool<SysUserInfoListVO> page = new Pagetool<>();
        page.setPageNumber(currentPage - 1);
        page.setPageSize(pageSize);
        page.setCurrentPageCount(currentPage);
        //参数
        List<Object> params = Lists.newArrayList();

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT u.id AS userId ,u.username AS username,u.nickname AS nickname, ");
        sql.append(" u.phone AS phone,u.`status` AS `status`,u.head_img_url AS headImgUrl, ");
        sql.append(" u.create_time AS createTime  FROM sys_user u  WHERE 1=1 ");

        if (!ValidationUtils.StrisNull(phone)) {
            params.add(phone);
            sql.append(" and u.phone= ? ");
        }
        if (!ValidationUtils.StrisNull(username)) {
            sql.append(" and u.username like '%"+username+"%'");
        }
        if (!ValidationUtils.StrisNull(nickname)) {
            sql.append(" and u.nickname like '%"+nickname+"%'");
        }
        if (!status.equals(2)) {
            params.add(status);
            sql.append(" and u.`status`= ? ");
        }
        sql.append(" ORDER BY u.create_time DESC ");
        pageForBean(jdbcTemplate, sql.toString(), page, SysUserInfoListVO.class,params.toArray());
        return page;

    }


    public Optional<SysUser> findByLoginAccount(String loginAccount) {
        return sysUserRepository.findByLoginAccount(loginAccount);
    }
}
