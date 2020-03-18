package com.cloud.admin.dao;

import com.cloud.admin.base.BaseDao;
import com.cloud.admin.repository.RoleMenuRepository;
import com.cloud.model.manage.RoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @ClassName : RoleMenuDao  //类名
 * @Description : 角色菜单Dao  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 11:19  //时间
 */
@Repository
public class RoleMenuDao extends BaseDao {

    @Autowired
    private RoleMenuRepository roleMenuRepository;


    public void deleteByRoleIds(List<Long> roleIds) {
        roleMenuRepository.deleteByRoleIds(roleIds);
    }

    public void saveAll(List<RoleMenu> roleMenuList) {
        roleMenuRepository.saveAll(roleMenuList);
    }

    public void deleteByMenuIds(List<Long> menuIdList) {
        roleMenuRepository.deleteByMenuIds(menuIdList);
    }

    public List<RoleMenu> findBySysRoleIds(List<Long> sysRoleIdList) {
        return roleMenuRepository.findBySysRoleIds(sysRoleIdList);
    }
}
