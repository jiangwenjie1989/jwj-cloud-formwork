package com.cloud.user.service;

import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.PageResponse;
import com.cloud.common.response.SimpleResponse;
import com.cloud.user.vo.permission.SysPermissionInfoVO;

public interface SysPermissionService {
    PageResponse<SysPermissionInfoVO> querySysPermissionPage(Integer currentPage, Integer pageSize, String permission, String name) throws Exception;

    ApiResponse<SysPermissionInfoVO> selectAllPermission() throws Exception;

    SimpleResponse addSysPermission(String permission, String name) throws Exception;

    SimpleResponse deleteSysPermission(String ids)throws Exception;

    SimpleResponse updateSysPermission(Long id, String name)throws Exception;

}

