package com.cloud.user.dao;



import com.cloud.common.response.Pagetool;
import com.cloud.common.utils.ValidationUtils;
import com.cloud.model.user.SysRole;
import com.cloud.user.base.BaseDao;
import com.cloud.user.repository.SysRoleRepository;
import com.cloud.user.vo.role.SysRoleInfoVO;
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
public class SysRoleDao extends BaseDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SysRoleRepository sysRoleRepository;


    public List<SysRole> findBySysUserIdList(List<Long> sysRoleIdList) {
        return sysRoleRepository.findBySysUserIdList(sysRoleIdList);
    }

    public Pagetool<SysRoleInfoVO> querySysRolePage(Integer currentPage, Integer pageSize, String code, String name) {
        Pagetool<SysRoleInfoVO> page = new Pagetool<>();
        page.setPageNumber(currentPage - 1);
        page.setPageSize(pageSize);
        page.setCurrentPageCount(currentPage);
        //参数
        List<Object> params = Lists.newArrayList();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT r.id AS id, r.`code` AS `code` ,r.`name` AS `name`,r.create_time AS createTime ");
        sql.append(" FROM sys_role r WHERE 1=1  ");

        if (!ValidationUtils.StrisNull(code)) {
            params.add(code);
            sql.append(" and r.code= ? ");
        }
        if (!ValidationUtils.StrisNull(name)) {
            sql.append(" and r.`name` like '%"+name+"%'");
        }

        sql.append(" ORDER BY r.create_time DESC ");
        pageForBean(jdbcTemplate, sql.toString(), page, SysRoleInfoVO.class,params.toArray());
        return page;
    }

    public Optional<SysRole> findByCode(String code) {
        return sysRoleRepository.findByCode(code);
    }

    public void save(SysRole sysRole) {
        sysRoleRepository.save(sysRole);
    }

    public Optional<SysRole> findById(Long id) {
        return sysRoleRepository.findById(id);
    }

    public void deleteByIdList(List<Long> idList) {
        sysRoleRepository.deleteByIdList(idList);
    }

    public List<SysRole> findAll() {
        return sysRoleRepository.findAll();
    }
}
