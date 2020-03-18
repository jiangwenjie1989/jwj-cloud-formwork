package com.cloud.user.service.impl;

import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.PageResponse;
import com.cloud.common.response.Pagetool;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.common.utils.DateUntil;
import com.cloud.model.user.SysPermission;
import com.cloud.user.dao.SysPermissionDao;
import com.cloud.user.dao.SysRolePermissionDao;
import com.cloud.user.service.SysPermissionService;
import com.cloud.user.vo.permission.SysPermissionInfoVO;
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
 * @ClassName : SysPermissionServiceImpl  //类名
 * @Description : 系统权限service  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 15:08  //时间
 */
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    private SysPermissionDao sysPermissionDao;

    @Autowired
    private SysRolePermissionDao sysRolePermissionDao;


    @Override
    public PageResponse<SysPermissionInfoVO> querySysPermissionPage(Integer currentPage, Integer pageSize, String permission, String name) throws Exception {
        PageResponse<SysPermissionInfoVO> resp = new PageResponse<>();
        Pagetool<SysPermissionInfoVO> pageList = sysPermissionDao.querySysPermissionPage(currentPage, pageSize,permission,name);
        if ( CollectionUtils.isEmpty(pageList.getList()) ) {
            return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "没有数据！");
        }
        resp.setPage(pageList);
        return resp;
    }

    @Override
    public ApiResponse<SysPermissionInfoVO> selectAllPermission()throws Exception {
        ApiResponse<SysPermissionInfoVO> resp = new ApiResponse<>();
        List<SysPermission> sysPermissionList = sysPermissionDao.findAll();
        if (CollectionUtils.isEmpty(sysPermissionList)) {
            return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "没有数据！");
        }
        List<SysPermissionInfoVO> sysPermissionInfoList = sysPermissionList.stream().map(sysPermission -> {
            SysPermissionInfoVO sysPermissionInfoVO = new SysPermissionInfoVO();
            BeanUtils.copyProperties(sysPermission,sysPermissionInfoVO);
            return sysPermissionInfoVO;
        }).collect(Collectors.toList());
        resp.setList(sysPermissionInfoList);
        return resp;
    }

    @Transactional
    @Override
    public SimpleResponse addSysPermission(String permission, String name) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        Optional<SysPermission> optP= sysPermissionDao.findByPermission(permission);
        if (optP.isPresent()) {
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "该权限标识存在！");
        }
        Timestamp nowTimestamp = DateUntil.getNowTimestamp();
        SysPermission sysPermission=new SysPermission();
        sysPermission.setPermission(permission);
        sysPermission.setName(name);
        sysPermission.setCreateTime(nowTimestamp);
        sysPermission.setUpdateTime(nowTimestamp);
        sysPermissionDao.save(sysPermission);
        return resp;
    }

    @Transactional
    @Override
    public SimpleResponse deleteSysPermission(String ids) throws Exception{
        SimpleResponse resp=new SimpleResponse();
        ArrayList<Long> idList = Lists.newArrayList();
        String[] split = ids.split(",");
        for (String s : split) {
            idList.add(Long.parseLong(s));
        }
        sysPermissionDao.deleteByIds(idList);
        sysRolePermissionDao.findBySysPermissionIdList(idList);
        return resp;
    }

    @Transactional
    @Override
    public SimpleResponse updateSysPermission(Long id, String name) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        Optional<SysPermission> optP = sysPermissionDao.findById(id);
        if (!optP.isPresent()) {
            return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "权限不存在！");
        }
        SysPermission sysPermission = optP.get();
        sysPermission.setUpdateTime(DateUntil.getNowTimestamp());
        sysPermission.setName(name);
        sysPermissionDao.save(sysPermission);
        return resp;
    }


}
