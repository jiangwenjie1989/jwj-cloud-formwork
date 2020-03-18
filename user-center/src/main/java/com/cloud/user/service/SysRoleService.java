package com.cloud.user.service;

import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.PageResponse;
import com.cloud.common.response.SimpleResponse;
import com.cloud.user.vo.role.SysRoleInfoVO;

public interface SysRoleService {
    PageResponse<SysRoleInfoVO> querySysRolePage(Integer currentPage, Integer pageSize, String code, String name) throws Exception;

    SimpleResponse addSysRole(String code, String name)throws Exception;

    SimpleResponse updateSysRole(Long id, String name)throws Exception;

    SimpleResponse deleteSysRole(String ids)throws Exception;

    SimpleResponse saveSysRolePermission(Long id, String permissionIds)throws Exception;

    SimpleResponse saveRoleMenu(Long id, String menuIds);

    ApiResponse<SysRoleInfoVO> selectAllSysRole();
}
