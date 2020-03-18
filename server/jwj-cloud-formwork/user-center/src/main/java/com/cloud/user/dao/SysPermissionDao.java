package com.cloud.user.dao;

import com.cloud.common.response.Pagetool;
import com.cloud.common.utils.ValidationUtils;
import com.cloud.model.user.SysPermission;
import com.cloud.user.base.BaseDao;
import com.cloud.user.repository.SysPermissionRepository;
import com.cloud.user.vo.permission.SysPermissionInfoVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName : SysPermissionDao  //类名
 * @Description : 权限Dao  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-05 12:52  //时间
 */
@Repository
public class SysPermissionDao extends BaseDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SysPermissionRepository sysPermissionRepository;


    public List<SysPermission> findByPermissionIdList(List<Long> sysPermissionIdList) {
        return sysPermissionRepository.findByPermissionIdList(sysPermissionIdList);
    }

    public Pagetool<SysPermissionInfoVO> querySysPermissionPage(Integer currentPage, Integer pageSize, String permission, String name) {

        Pagetool<SysPermissionInfoVO> page = new Pagetool<>();
        page.setPageNumber(currentPage - 1);
        page.setPageSize(pageSize);
        page.setCurrentPageCount(currentPage);
        //参数
        List<Object> params = Lists.newArrayList();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT p.id AS id,p.permission AS permission,p.`name` AS `name`,p.create_time AS createTime ");
        sql.append(" FROM sys_permission p WHERE 1=1  ");

        if (!ValidationUtils.StrisNull(permission)) {
            params.add(permission);
            sql.append(" and p.permission= ? ");
        }
        if (!ValidationUtils.StrisNull(name)) {
            sql.append(" and p.`name` like '%"+name+"%'");
        }
        sql.append(" ORDER BY p.create_time DESC ");
        pageForBean(jdbcTemplate, sql.toString(), page, SysPermissionInfoVO.class,params.toArray());
        return page;



    }

    public List<SysPermission> findAll() {
        return sysPermissionRepository.findAll();
    }


    public Optional<SysPermission> findByPermission(String permission) {
        return sysPermissionRepository.findByPermission(permission);
    }

    public void save(SysPermission sysPermission) {
        sysPermissionRepository.save(sysPermission);
    }

    public void deleteByIds(ArrayList<Long> idList) {
        sysPermissionRepository.deleteByIds(idList);
    }

    public Optional<SysPermission> findById(Long id) {
        return sysPermissionRepository.findById(id);
    }
}
