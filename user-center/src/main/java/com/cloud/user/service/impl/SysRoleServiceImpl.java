package com.cloud.user.service.impl;

import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.PageResponse;
import com.cloud.common.response.Pagetool;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.common.utils.DateUntil;
import com.cloud.model.user.SysRole;
import com.cloud.model.user.SysRolePermission;
import com.cloud.user.dao.SysRoleDao;
import com.cloud.user.dao.SysRolePermissionDao;
import com.cloud.user.dao.SysUserRoleDao;
import com.cloud.user.exception.BusinessException;
import com.cloud.user.feign.ManageClient;
import com.cloud.user.service.SysRoleService;
import com.cloud.user.vo.role.SysRoleInfoVO;
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
 * @ClassName : SysRoleServiceImpl  //类名
 * @Description : 角色实现类  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 09:27  //时间
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Autowired
    private SysRolePermissionDao sysRolePermissionDao;

    @Autowired
    private ManageClient manageClient;

    @Override
    public PageResponse<SysRoleInfoVO> querySysRolePage(Integer currentPage, Integer pageSize, String code, String name) {
        PageResponse<SysRoleInfoVO> resp = new PageResponse<>();
        Pagetool<SysRoleInfoVO> pageList = sysRoleDao.querySysRolePage(currentPage, pageSize,code,name);
        if ( CollectionUtils.isEmpty(pageList.getList()) ) {
            return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "没有数据！");
        }
        resp.setPage(pageList);
        return resp;
    }

    @Override
    public ApiResponse<SysRoleInfoVO> selectAllSysRole() {
        ApiResponse<SysRoleInfoVO> resp = new ApiResponse<>();
        List<SysRole> sysRoleList = sysRoleDao.findAll();
        if (CollectionUtils.isEmpty(sysRoleList)) {
            return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "没有数据！");
        }
        List<SysRoleInfoVO> sysRoleListVOList = sysRoleList.stream().map(sysRole -> {
            SysRoleInfoVO sysRoleInfoVO = new SysRoleInfoVO();
            BeanUtils.copyProperties(sysRole,sysRoleInfoVO);
            return sysRoleInfoVO;
        }).collect(Collectors.toList());
        resp.setList(sysRoleListVOList);
        return resp;
    }


    @Transactional
    @Override
    public SimpleResponse addSysRole(String code, String name) throws Exception {
        SimpleResponse resp = new SimpleResponse();
        Timestamp nowTimestamp = DateUntil.getNowTimestamp();

        Optional<SysRole> optRole= sysRoleDao.findByCode(code);
        if (optRole.isPresent()) {
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "角色code存在！");
        }
        SysRole sysRole = new SysRole();
        sysRole.setCode(code);
        sysRole.setName(name);
        sysRole.setCreateTime(nowTimestamp);
        sysRole.setUpdateTime(nowTimestamp);
        sysRoleDao.save(sysRole);
        return  resp;

    }

    @Transactional
    @Override
    public SimpleResponse updateSysRole(Long id, String name) throws Exception {
        SimpleResponse resp = new SimpleResponse();
        Optional<SysRole> optRole= sysRoleDao.findById(id);
        if (!optRole.isPresent()) {
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "角色不存在！");
        }
        SysRole sysRole = optRole.get();
        sysRole.setName(name);
        sysRole.setUpdateTime(DateUntil.getNowTimestamp());
        sysRoleDao.save(sysRole);
        return resp;
    }

    @Transactional
    @Override
    public SimpleResponse deleteSysRole(String ids) throws Exception {
        SimpleResponse resp = new SimpleResponse();
        ArrayList<Long> idList = Lists.newArrayList();
        String[] split = ids.split(",");
        for (String s : split) {
            idList.add(Long.parseLong(s));
        }
        sysRoleDao.deleteByIdList(idList);
        //删除角色权限关系表
        sysRolePermissionDao.deleteBySysRoleId(idList);
        //删除用户角色系表
        sysUserRoleDao.deleteBySysRoleId(idList);
        //删除角色菜单关系表
        SimpleResponse simpleResponse = manageClient.deleteRoleMenu(ids);
        if(SysRespStatusE.失败.getDesc().equals(simpleResponse.getStatus())){
            throw new BusinessException(simpleResponse.getCode(),simpleResponse.getMessage());
        }
        return resp;
    }

    @Transactional
    @Override
    public SimpleResponse saveSysRolePermission(Long id, String permissionIds) throws Exception {
        SimpleResponse resp = new SimpleResponse();
        Optional<SysRole> optRole= sysRoleDao.findById(id);
        if (!optRole.isPresent()) {
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "角色不存在！");
        }
        sysRolePermissionDao.deleteBySysRoleId(Lists.newArrayList(id));

        Timestamp nowTimestamp = DateUntil.getNowTimestamp();
        ArrayList<Long> permissionIdsList = Lists.newArrayList();
        String[] split = permissionIds.split(",");
        for (String s : split) {
            permissionIdsList.add(Long.parseLong(s));
        }
        //创建角色权限集合
        List<SysRolePermission> sysRolePermissionList = permissionIdsList.stream().map(permissionId -> {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setSysRoleId(id);
            sysRolePermission.setPermissionId(permissionId);
            sysRolePermission.setCreateTime(nowTimestamp);
            return sysRolePermission;
        }).collect(Collectors.toList());

        sysRolePermissionDao.saveAll(sysRolePermissionList);
        return resp;
    }

    @Transactional
    @Override
    public SimpleResponse saveRoleMenu(Long id, String menuIds) {
        SimpleResponse resp = new SimpleResponse();
        Optional<SysRole> optRole= sysRoleDao.findById(id);
        if (!optRole.isPresent()) {
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "角色不存在！");
        }
        SimpleResponse simpleResponse=manageClient.saveRoleMenu(id,menuIds);
        if(SysRespStatusE.失败.getDesc().equals(simpleResponse.getStatus())){
            throw new BusinessException(simpleResponse.getCode(),simpleResponse.getMessage());
        }
        return resp;
    }


}
