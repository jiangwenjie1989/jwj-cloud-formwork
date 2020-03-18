package com.cloud.admin.service;

import com.cloud.admin.vo.menu.MenuVO;
import com.cloud.admin.vo.menu.UserMenuListVO;
import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.PageResponse;
import com.cloud.common.response.SimpleResponse;

public interface MenuService {

    SimpleResponse deleteRoleMenu(String roleIds)throws Exception;

    SimpleResponse saveRoleMenu(Long roleId, String menuIds)throws Exception;

    PageResponse<MenuVO> queryMenuPage(Integer currentPage, Integer pageSize, String name)throws Exception;

    ApiResponse<MenuVO> selectAllMenu()throws Exception;

    SimpleResponse addMenu(Long parentId, String name, String url, String css, Integer sort);

    SimpleResponse deleteMenu(Long id);

    ApiResponse<UserMenuListVO> selectUserMenu();
}
