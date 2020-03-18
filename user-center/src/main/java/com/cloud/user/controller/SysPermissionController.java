package com.cloud.user.controller;

import com.cloud.common.annotation.AuthorizeAnnotation;
import com.cloud.common.annotation.CacheLock;
import com.cloud.common.annotation.CacheParam;
import com.cloud.common.annotation.LogAnnotation;
import com.cloud.common.constants.SwggerCommonTags;
import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.PageResponse;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.common.utils.ValidationUtils;
import com.cloud.user.service.SysPermissionService;
import com.cloud.user.vo.permission.SysPermissionInfoVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : SysPermissionController  //类名
 * @Description : 系统权限控制层 //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 13:46  //时间
 */
@RequestMapping(value = "/api/admin")
@RestController
public class SysPermissionController {



    @Autowired
    private SysPermissionService sysPermissionService;

    @AuthorizeAnnotation(hasAuthority = "back:permission:query")
    @ApiOperation(value = "获取角色信息列表接口--(分页)", httpMethod = "POST",tags=SwggerCommonTags.SYS_PERMISSION_MODULE)
    @RequestMapping(value = "/querySysPermissionPage.do",method=RequestMethod.POST)
    public PageResponse<SysPermissionInfoVO> querySysPermissionPage(
            @ApiParam(required=true, name="currentPage", value="当前页 必填")
            @RequestParam(defaultValue = "1") Integer currentPage,

            @ApiParam(required=true, name="pageSize", value="每页显示条数 默认是10条")
            @RequestParam(defaultValue = "10") Integer pageSize,

            @ApiParam(required=false, name="permission", value="权限标识 查询条件非必填")
            @RequestParam(defaultValue = "") String permission,

            @ApiParam(required=false, name="name", value="权限名称 查询条件非必填")
            @RequestParam(defaultValue = "") String name
    ) throws Exception{
        return sysPermissionService.querySysPermissionPage(currentPage, pageSize,permission,name);
    }

    @ApiOperation(value = "获取所有权限接口", httpMethod = "GET",tags=SwggerCommonTags.SYS_PERMISSION_MODULE)
    @RequestMapping(value = "/selectAllPermission.do",method=RequestMethod.GET)
    public ApiResponse<SysPermissionInfoVO> selectAllPermission() throws Exception{
        return sysPermissionService.selectAllPermission();
    }

    @CacheLock(prefix="back:permission:add")
    @AuthorizeAnnotation(hasAuthority = "back:permission:add")
    @LogAnnotation(module = "添加权限")
    @ApiOperation(value = "添加权限接口", httpMethod = "POST",tags= SwggerCommonTags.SYS_PERMISSION_MODULE)
    @RequestMapping(value = "/addSysPermission.do",method= RequestMethod.POST)
    public SimpleResponse addSysPermission(
            @CacheParam(name="permission")
            @ApiParam(required=true, name="permission", value="权限标识 必填")
            @RequestParam(defaultValue = "") String permission,

            @CacheParam(name="name")
            @ApiParam(required=true, name="name", value="权限名称 必填")
            @RequestParam(defaultValue = "") String name
    ) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(ValidationUtils.isStrsNull(permission,name)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "参数传入错误!");
        }
        return sysPermissionService.addSysPermission(permission,name);
    }

    @AuthorizeAnnotation(hasAuthority = "back:permission:delete")
    @LogAnnotation(module = "删除权限")
    @ApiOperation(value = "删除权限接口", httpMethod = "POST",tags= SwggerCommonTags.SYS_PERMISSION_MODULE)
    @RequestMapping(value = "/deleteSysPermission.do",method= RequestMethod.POST)
    public SimpleResponse deleteSysPermission(
            @ApiParam(required=true, name="ids", value="权限ids 必填 多个用,号隔开 例如 1,2,3,4")
            @RequestParam(defaultValue = "") String ids
    ) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(ValidationUtils.isStrsNull(ids)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "权限ids为空!");
        }
        return sysPermissionService.deleteSysPermission(ids);
    }

    @AuthorizeAnnotation(hasAuthority = "back:permission:update")
    @LogAnnotation(module = "修改权限")
    @ApiOperation(value = "修改权限接口", httpMethod = "POST",tags= SwggerCommonTags.SYS_PERMISSION_MODULE)
    @RequestMapping(value = "/updateSysPermission.do",method= RequestMethod.POST)
    public SimpleResponse updateSysPermission(
            @ApiParam(required=true, name="id", value="权限id 必填")
            @RequestParam(defaultValue = "0") Long id,

            @ApiParam(required=true, name="name", value="权限名称 必填")
            @RequestParam(defaultValue = "") String name
    ) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(ValidationUtils.isStrsNull(name)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "权限名称名称!");
        }
        if(id.equals(0L)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "权限id 为空!");
        }
        return sysPermissionService.updateSysPermission(id,name);
    }



}
