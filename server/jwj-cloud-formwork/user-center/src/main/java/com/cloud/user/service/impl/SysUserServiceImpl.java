package com.cloud.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloud.common.bean.SysUserInfo;
import com.cloud.common.constants.RedisCacheKeys;
import com.cloud.common.constants.CommConstants;
import com.cloud.common.enumeration.SysUserLoginTypeE;
import com.cloud.common.enumeration.SysUserStatusE;
import com.cloud.common.redis.RedisStringCacheSupport;
import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.PageResponse;
import com.cloud.common.response.Pagetool;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.common.utils.DateUntil;
import com.cloud.common.utils.ThreadLocalUtil;
import com.cloud.common.utils.TokenUtil;
import com.cloud.common.utils.ValidationUtils;
import com.cloud.model.user.*;
import com.cloud.user.dao.*;
import com.cloud.user.service.SysUserService;
import com.cloud.user.vo.permission.SysPermissionInfoVO;
import com.cloud.user.vo.role.SysRoleInfoVO;
import com.cloud.user.vo.user.SysUserInfoListVO;
import com.cloud.user.vo.user.SysUserInfoVO;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName : SysUserServiceImpl  //类名
 * @Description : 系统用户实现  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-05 13:46  //时间
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private RedisStringCacheSupport cacheStr;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserCredentialsDao sysUserCredentialsDao;

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Autowired
    private SysRolePermissionDao sysRolePermissionDao;

    @Autowired
    private SysPermissionDao sysPermissionDao;

    @Transactional
    @Override
    public SimpleResponse addSysUser(String phone, String password, String username, String nickname, String headImgUrl) {
        SimpleResponse resp = new SimpleResponse();
        Timestamp nowTimestamp = DateUntil.getNowTimestamp();

        Optional<SysUser> byPhone = sysUserDao.findByPhone(phone);
        if (byPhone.isPresent()) {
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "手机号存在!");
        }
        Optional<SysUser> byUsername = sysUserDao.findByUsername(username);
        if (byUsername.isPresent()) {
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "用户名存在!");
        }

        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPassword(password);
        sysUser.setNickname(nickname);
        sysUser.setHeadImgUrl(headImgUrl);
        sysUser.setPhone(phone);
        sysUser.setStatus(SysUserStatusE.有效.value);
        sysUser.setCreateTime(nowTimestamp);
        sysUser.setUpdateTime(nowTimestamp);
        SysUser sysUserDB = sysUserDao.save(sysUser);
        Long userDBIdId = sysUserDB.getId();

        //保存用户凭证表
        SysUserCredentials credentialsUsername = new SysUserCredentials();
        credentialsUsername.setLoginType(SysUserLoginTypeE.用户名登录.value);
        credentialsUsername.setLoginAccount(username);
        credentialsUsername.setSysUserId(userDBIdId);
        credentialsUsername.setCreateTime(nowTimestamp);

        SysUserCredentials credentialsPhone = new SysUserCredentials();
        credentialsPhone.setLoginType(SysUserLoginTypeE.手机号登录.value);
        credentialsPhone.setLoginAccount(phone);
        credentialsPhone.setSysUserId(userDBIdId);
        credentialsPhone.setCreateTime(nowTimestamp);

        ArrayList<SysUserCredentials> sysUserCredentialsList = Lists.newArrayList();
        sysUserCredentialsList.add(credentialsUsername);
        sysUserCredentialsList.add(credentialsPhone);
        sysUserCredentialsDao.saveAll(sysUserCredentialsList);
        return resp;
    }

    @Transactional
    @Override
    public SimpleResponse updateSysUser(Long userId, String password, String nickname, String headImgUrl) {
        SimpleResponse resp = new SimpleResponse();
        Timestamp nowTimestamp = DateUntil.getNowTimestamp();
        Optional<SysUser> optU = sysUserDao.findById(userId);
        if(!optU.isPresent()){
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "该用户不存在!");
        }
        SysUser sysUser = optU.get();
        if(!ValidationUtils.StrisNull(password)){sysUser.setUsername(password);}
        if(!ValidationUtils.StrisNull(nickname)){sysUser.setUsername(nickname);}
        if(!ValidationUtils.StrisNull(headImgUrl)){sysUser.setUsername(headImgUrl);}
        sysUser.setUpdateTime(nowTimestamp);
        sysUserDao.save(sysUser);
        return resp;
    }

    /**
     * //修改用户凭证表
     * @param loginAccount
     * @param sysUserId
     */
    private void updateSysUserCredentials(String loginAccount, Long sysUserId) {
        Optional<SysUserCredentials> optionalCredentials = sysUserCredentialsDao.findBySysUserIdAndLoginAccount(sysUserId, loginAccount);
        if (optionalCredentials.isPresent()) {
            SysUserCredentials sysUserCredentials = optionalCredentials.get();
            sysUserCredentials.setLoginAccount(loginAccount);
            sysUserCredentialsDao.save(sysUserCredentials);
        }
    }

    @Override
    public PageResponse<SysUserInfoListVO> querySysUserPage(Integer currentPage, Integer pageSize, String phone, String username, String nickname, Integer status) {
        PageResponse<SysUserInfoListVO> resp = new PageResponse<>();
        Pagetool<SysUserInfoListVO> pageList = sysUserDao.querySysUserPage(currentPage, pageSize,phone,username,nickname,status);
        if ( CollectionUtils.isEmpty(pageList.getList()) ) {
            return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "没有数据！");
        }
        resp.setPage(pageList);
        return resp;
    }

    @Override
    public ApiResponse<SysUserInfoVO> sysUserLogin(String loginAccount, String password)throws Exception {
        ApiResponse<SysUserInfoVO>  resp = new ApiResponse<>();
        SysUserInfoVO sysUserInfoVO = new SysUserInfoVO();

        Optional<SysUser> optU=sysUserDao.findByLoginAccount(loginAccount);
        if(!optU.isPresent()){
            return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "用户名或者密码有误！");
        }
        SysUser sysUser = optU.get();
        String passwordDB = sysUser.getPassword();
        if(!passwordDB.equals(password)){
            return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "用户名或者密码有误！");
        }
        Long sysUserId = sysUser.getId();
        String username = sysUser.getUsername();
        Integer status = sysUser.getStatus();
        if(!username.equals(CommConstants.SUPER_ADMIN)){
            //设置用户角色集合和权限集合信息
            setUserRoleListAndPermissionList(sysUserInfoVO, sysUserId);
        }

        Map<String, Object> tokenMap=new HashMap<>();
        tokenMap.put("userId",sysUserId);
        tokenMap.put("username",username);
        tokenMap.put("status",status);
        String token = TokenUtil.createToken(tokenMap);

        sysUserInfoVO.setHeadImgUrl(sysUser.getHeadImgUrl());
        sysUserInfoVO.setNickname(sysUser.getNickname());
        sysUserInfoVO.setPhone(sysUser.getPhone());
        sysUserInfoVO.setStatus(status);
        sysUserInfoVO.setUsername(username);
        sysUserInfoVO.setToken(token);
        resp.setData(sysUserInfoVO);
        //将token信息缓存
        cacheStr.put(RedisCacheKeys.USER_ADMIN_TOKEN_KEY+sysUserId,token,86400L);

        //系统用户信息bean 每个系统拦截器需要用到
        SysUserInfo sysUserInfo = new SysUserInfo();
        BeanUtils.copyProperties(sysUserInfoVO, sysUserInfo);
        cacheStr.put(RedisCacheKeys.USER_ADMIN_INFO_KEY+sysUserId,JSONObject.toJSONString(sysUserInfo),86400L);

        return resp;
    }

    @Override
    public ApiResponse<SysUserInfoVO> getSysUserInfo() {
        ApiResponse<SysUserInfoVO>  resp = new ApiResponse<>();
        SysUserInfoVO sysUserInfoVO = new SysUserInfoVO();
        Long userId = ThreadLocalUtil.getUserIdHolder();
        Optional<SysUser> optU = sysUserDao.findById(userId);
        if(!optU.isPresent()){
            return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "该用户不存在！");
        }
        String cacheJson = cacheStr.getCached(RedisCacheKeys.USER_ADMIN_INFO_KEY + userId);
        if (!ValidationUtils.StrisNull(cacheJson)){
            SysUserInfo sysUserInfo = JSON.parseObject(cacheJson, SysUserInfo.class);
            BeanUtils.copyProperties(sysUserInfo, sysUserInfoVO);
            resp.setData(sysUserInfoVO);
        }
        return resp;
    }

    @Override
    public SimpleResponse saveUserRole(Long userId, String roleIds) {
        SimpleResponse resp = new SimpleResponse();
        Optional<SysUser> optU = sysUserDao.findById(userId);
        if(!optU.isPresent()){
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "该用户不存在!");
        }
        sysUserRoleDao.deleteBySysUserId(userId);
        Timestamp nowTimestamp = DateUntil.getNowTimestamp();
        ArrayList<Long> roleIdsList = Lists.newArrayList();
        String[] split = roleIds.split(",");
        for (String s : split) {
            roleIdsList.add(Long.parseLong(s));
        }
        List<SysUserRole> sysUserRoleList = roleIdsList.stream().map(roleId -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setSysUserId(userId);
            sysUserRole.setSysRoleId(roleId);
            sysUserRole.setCreateTime(nowTimestamp);
            return sysUserRole;
        }).collect(Collectors.toList());
        sysUserRoleDao.saveAll(sysUserRoleList);
        return resp;
    }


    /**
     * 设置用户角色集合和权限集合信息
     * @param sysUserInfoVO
     * @param sysUserId
     */
    private void setUserRoleListAndPermissionList(SysUserInfoVO sysUserInfoVO, Long sysUserId) {
        List<SysUserRole> sysUserRoleList=sysUserRoleDao.findBySysUserId(sysUserId);
        if(!CollectionUtils.isEmpty(sysUserRoleList)){
            //获取用户所有角色id集合
            List<Long> sysRoleIdList=sysUserRoleList.stream().map(sysUserRole -> sysUserRole.getSysRoleId()).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(sysRoleIdList)){
                //查询用户所有角色对象集合
                List<SysRole> sysRoleList=sysRoleDao.findBySysUserIdList(sysRoleIdList);
                if(!CollectionUtils.isEmpty(sysRoleList)){
                    //将orm对象集合转换成vo对象集合
                    List<SysRoleInfoVO> sysRoleInfoVOList = sysRoleList.stream().map(sysRole -> {
                        SysRoleInfoVO sysRoleInfoVO = new SysRoleInfoVO();
                        BeanUtils.copyProperties(sysRole, sysRoleInfoVO);
                        return sysRoleInfoVO;
                    }).collect(Collectors.toList());
                    sysUserInfoVO.setSysRoleList(sysRoleInfoVOList);
                }
                //根据角色id集合获取 角色权限关系集合
                List<SysRolePermission> sysRolePermissionList=sysRolePermissionDao.findBySysRoleIdList(sysRoleIdList);
                if(!CollectionUtils.isEmpty(sysRolePermissionList)){
                    //获所有权限id集合
                    List<Long> sysPermissionIdList=sysRolePermissionList.stream().map(sysRolePermission -> sysRolePermission.getPermissionId()).collect(Collectors.toList());
                    if(!CollectionUtils.isEmpty(sysPermissionIdList)){
                        //获取所有权限
                        List<SysPermission> sysPermissionList=sysPermissionDao.findByPermissionIdList(sysPermissionIdList);
                        if(!CollectionUtils.isEmpty(sysPermissionList)){
                            //将权限orm对象集合转换成权限vo对象集合
                            List<SysPermissionInfoVO> sysPermissionInfoVOList = sysPermissionList.stream().map(sysPermission -> {
                                SysPermissionInfoVO sysPermissionInfoVO = new SysPermissionInfoVO();
                                BeanUtils.copyProperties(sysPermission, sysPermissionInfoVO);
                                return sysPermissionInfoVO;
                            }).collect(Collectors.toList());
                            sysUserInfoVO.setSysPermissionList(sysPermissionInfoVOList);
                        }
                    }
                }
            }
        }
    }


}
