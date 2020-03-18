package com.cloud.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.cloud.admin.dao.MenuDao;
import com.cloud.admin.dao.RoleMenuDao;
import com.cloud.admin.service.MenuService;
import com.cloud.admin.vo.menu.MenuInfoVO;
import com.cloud.admin.vo.menu.MenuVO;
import com.cloud.admin.vo.menu.UserMenuListVO;
import com.cloud.common.bean.SysRoleInfo;
import com.cloud.common.bean.SysUserInfo;
import com.cloud.common.constants.CommConstants;
import com.cloud.common.constants.RedisCacheKeys;
import com.cloud.common.redis.RedisStringCacheSupport;
import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.PageResponse;
import com.cloud.common.response.Pagetool;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.common.utils.DateUntil;
import com.cloud.common.utils.ThreadLocalUtil;
import com.cloud.model.manage.Menu;
import com.cloud.model.manage.RoleMenu;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName : MenuServiceImpl  //类名
 * @Description : 菜单service实现类  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 17:18  //时间
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private RoleMenuDao roleMenuDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private RedisStringCacheSupport cacheString;


    @Transactional
    @Override
    public SimpleResponse deleteRoleMenu(String roleIds)throws Exception {
        SimpleResponse resp=new SimpleResponse();
        ArrayList<Long> idList = Lists.newArrayList();
        String[] split = roleIds.split(",");
        for (String s : split) {
            idList.add(Long.parseLong(s));
        }
        roleMenuDao.deleteByRoleIds(idList);
        return resp;
    }

    @Transactional
    @Override
    public SimpleResponse saveRoleMenu(Long roleId, String menuIds)throws Exception {
        SimpleResponse resp=new SimpleResponse();
        roleMenuDao.deleteByRoleIds(Lists.newArrayList(roleId));

        ArrayList<Long> menuIdList = Lists.newArrayList();
        String[] split = menuIds.split(",");
        for (String s : split) {
            menuIdList.add(Long.parseLong(s));
        }
        //创建角色权限集合
        List<RoleMenu> roleMenuList = menuIdList.stream().map(menuId -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setSysRoleId(roleId);
            roleMenu.setMenuId(menuId);
            return roleMenu;
        }).collect(Collectors.toList());
        roleMenuDao.saveAll(roleMenuList);
        return resp;
    }

    @Override
    public PageResponse<MenuVO> queryMenuPage(Integer currentPage, Integer pageSize, String name) throws Exception{
        PageResponse<MenuVO> resp = new PageResponse<>();
        Pagetool<MenuVO> pageList = menuDao.queryMenuPage(currentPage, pageSize,name);
        if ( CollectionUtils.isEmpty(pageList.getList()) ) {
            return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "没有数据！");
        }
        resp.setPage(pageList);
        return resp;
    }

    @Override
    public ApiResponse<MenuVO> selectAllMenu()throws Exception {
        ApiResponse<MenuVO> resp = new ApiResponse<>();
        List<Menu> sysPermissionList = menuDao.findAll();
        if (CollectionUtils.isEmpty(sysPermissionList)) {
            return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "没有数据！");
        }
        List<MenuVO> sysPermissionInfoList = sysPermissionList.stream().map(menu -> {
            MenuVO menuVO = new MenuVO();
            BeanUtils.copyProperties(menu,menuVO);
            return menuVO;
        }).collect(Collectors.toList());
        resp.setList(sysPermissionInfoList);
        return resp;
    }

    @Transactional
    @Override
    public SimpleResponse addMenu(Long parentId, String name, String url, String css, Integer sort) {
        SimpleResponse resp=new SimpleResponse();
        if(!parentId.equals(0L)){
            Optional<Menu> optMenu=menuDao.findById(parentId);
            if (!optMenu.isPresent()) {
                return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "父权限id不存在！");
            }
        }
        Menu menu=new Menu();
        Timestamp nowTimestamp = DateUntil.getNowTimestamp();
        menu.setParentId(parentId);
        menu.setName(name);
        menu.setUrl(url);
        menu.setCss(css);
        menu.setSort(sort);
        menu.setCreateTime(nowTimestamp);
        menu.setUpdateTime(nowTimestamp);
        menuDao.save(menu);
        return resp;
    }

    @Transactional
    @Override
    public SimpleResponse deleteMenu(Long id) {
        SimpleResponse resp=new SimpleResponse();
        Optional<Menu> optMenu = menuDao.findById(id);
        if (!optMenu.isPresent()) {
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "菜单不存在！");
        }
        //所有子菜单
        List<Menu> menuList = menuDao.findByParentId(id);
        if(!CollectionUtils.isEmpty(menuList)){
            menuDao.deleteAll(menuList);
            //删除角色菜单关系表
            List<Long> menuIdList=menuList.stream().map(menu -> menu.getId()).collect(Collectors.toList());
            roleMenuDao.deleteByMenuIds(menuIdList);
        }
        //删除自己 角色菜单关系表
        menuDao.deleteById(id);
        roleMenuDao.deleteByMenuIds(Lists.newArrayList(id));
        return resp;
    }

    @Override
    public ApiResponse<UserMenuListVO> selectUserMenu() {
        ApiResponse<UserMenuListVO> resp=new ApiResponse<>();
        String username = ThreadLocalUtil.getUsernameHolder();
        //超级管理员
        if(CommConstants.SUPER_ADMIN.equals(username)){
            List<Menu> allMenu = menuDao.findAll();
            if(!CollectionUtils.isEmpty(allMenu)){
                setUserMenu(resp, allMenu);
            }else {
                return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "没有数据！");
            }
        }else {
            Long userId = ThreadLocalUtil.getUserIdHolder();
            String cacheJson = cacheString.getCached(RedisCacheKeys.USER_ADMIN_INFO_KEY + userId);
            if(cacheJson!=null){
                SysUserInfo sysUserInfo = JSON.parseObject(cacheJson, SysUserInfo.class);
                List<SysRoleInfo> sysRoleList = sysUserInfo.getSysRoleList();
                if(!CollectionUtils.isEmpty(sysRoleList)){
                    List<Long> sysRoleIdList=sysRoleList.stream().map(sysRoleInfo -> sysRoleInfo.getId()).collect(Collectors.toList());
                    List<RoleMenu> allRoleMenu = roleMenuDao.findBySysRoleIds(sysRoleIdList);
                    if(!CollectionUtils.isEmpty(allRoleMenu)){
                        List<Long> menuIdList=allRoleMenu.stream().map(roleMenu -> roleMenu.getMenuId()).collect(Collectors.toList());
                        List<Menu> allMenu = menuDao.findByIds(menuIdList);
                        if(!CollectionUtils.isEmpty(allMenu)){
                            setUserMenu(resp, allMenu);
                        }else {
                            return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "没有数据！");
                        }
                    }
                }
            }
        }

        return resp;
    }

    /**
     * 设置用户菜单
     * @param resp
     * @param allMenu
     */
    private void setUserMenu(ApiResponse<UserMenuListVO> resp, List<Menu> allMenu) {
        List<Menu> firstMenuList = allMenu.stream().filter((menu) -> menu.getParentId().equals(0L)).collect(Collectors.toList());

        List<UserMenuListVO> firstMenuInfoVOList = firstMenuList.stream().map(menu -> {
            UserMenuListVO userMenuListVO = new UserMenuListVO();
            BeanUtils.copyProperties(menu,userMenuListVO);
            //二级菜单
            List<Menu> twoMenuList = allMenu.stream().filter((twoMenu) -> twoMenu.getParentId().equals(menu.getId())).collect(Collectors.toList());

            List<MenuInfoVO> twoMenuInfoVOList = twoMenuList.stream().map(twoMenu -> {
                MenuInfoVO menuInfoVO = new MenuInfoVO();
                BeanUtils.copyProperties(twoMenu,menuInfoVO);
                return menuInfoVO;
            }).collect(Collectors.toList());
            userMenuListVO.setTwoMenuList(twoMenuInfoVOList);
            return userMenuListVO;

        }).collect(Collectors.toList());

        resp.setList(firstMenuInfoVOList);
    }


}
