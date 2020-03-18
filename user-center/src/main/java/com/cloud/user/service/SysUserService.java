package com.cloud.user.service;


import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.PageResponse;
import com.cloud.common.response.SimpleResponse;
import com.cloud.user.vo.user.SysUserInfoListVO;
import com.cloud.user.vo.user.SysUserInfoVO;


public interface SysUserService {

    SimpleResponse updateSysUser(Long userId, String password, String nickname, String headImgUrl)throws Exception;

    PageResponse<SysUserInfoListVO> querySysUserPage(Integer currentPage, Integer pageSize, String phone, String username, String nickname, Integer status)throws Exception;

    ApiResponse<SysUserInfoVO> sysUserLogin(String loginAccount, String password)throws Exception;

    SimpleResponse addSysUser(String phone, String password, String username, String nickname, String headImgUrl)throws Exception;

    ApiResponse<SysUserInfoVO> getSysUserInfo()throws Exception;

    SimpleResponse saveUserRole(Long userId, String roleIds);

}

