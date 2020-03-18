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
import com.cloud.user.service.SysRoleService;
import com.cloud.user.vo.role.SysRoleInfoVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : SysRoleController  //类名
 * @Description : 系统用户角色控制层 //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-05 13:46  //时间
 */
@RequestMapping(value = "/api/admin")
@RestController
public class SysRoleController {


    @Autowired
    private SysRoleService sysRoleService;

    @AuthorizeAnnotation(hasAuthority = "back:role:query")
    @ApiOperation(value = "获取角色信息列表接口--(分页)", httpMethod = "POST",tags=SwggerCommonTags.SYS_ROLE_MODULE)
    @RequestMapping(value = "/querySysRolePage.do",method=RequestMethod.POST)
    public PageResponse<SysRoleInfoVO> querySysRolePage(
            @ApiParam(required=true, name="currentPage", value="当前页 必填")
            @RequestParam(defaultValue = "1") Integer currentPage,

            @ApiParam(required=true, name="pageSize", value="每页显示条数 默认是10条")
            @RequestParam(defaultValue = "10") Integer pageSize,

            @ApiParam(required=false, name="phone", value="手机号 查询条件非必填")
            @RequestParam(defaultValue = "") String code,

            @ApiParam(required=false, name="name", value="用户名 查询条件非必填")
            @RequestParam(defaultValue = "") String name
    ) throws Exception{
        return sysRoleService.querySysRolePage(currentPage, pageSize,code,name);
    }

    @ApiOperation(value = "获取所有角色接口", httpMethod = "GET",tags=SwggerCommonTags.SYS_ROLE_MODULE)
    @RequestMapping(value = "/selectAllSysRole.do",method=RequestMethod.GET)
    public ApiResponse<SysRoleInfoVO> selectAllSysRole() throws Exception{
        return sysRoleService.selectAllSysRole();
    }


    @CacheLock(prefix="back:role:add")
    @AuthorizeAnnotation(hasAuthority = "back:role:add")
    @LogAnnotation(module = "添加角色")
    @ApiOperation(value = "添加角色接口", httpMethod = "POST",tags= SwggerCommonTags.SYS_ROLE_MODULE)
    @RequestMapping(value = "/addSysRole.do",method= RequestMethod.POST)
    public SimpleResponse addSysRole(

            @CacheParam(name="code")
            @ApiParam(required=true, name="code", value="角色code 必填")
            @RequestParam(defaultValue = "") String code,

            @CacheParam(name="name")
            @ApiParam(required=true, name="name", value="角色名称 必填")
            @RequestParam(defaultValue = "") String name
    ) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(ValidationUtils.isStrsNull(code,name)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "参数传入错误!");
        }
        return sysRoleService.addSysRole(code,name);
    }

    @AuthorizeAnnotation(hasAuthority = "back:role:update")
    @LogAnnotation(module = "修改角色")
    @ApiOperation(value = "修改角色接口", httpMethod = "POST",tags= SwggerCommonTags.SYS_ROLE_MODULE)
    @RequestMapping(value = "/updateSysRole.do",method= RequestMethod.POST)
    public SimpleResponse updateSysRole(
            @ApiParam(required=true, name="id", value="角色id 必填")
            @RequestParam(defaultValue = "0") Long id,

            @ApiParam(required=true, name="name", value="角色名称 必填")
            @RequestParam(defaultValue = "") String name
    ) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(ValidationUtils.isStrsNull(name)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "参数传入错误!");
        }
        if(id.equals(0L)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "角色id为空!");
        }
        return sysRoleService.updateSysRole(id,name);
    }

    @AuthorizeAnnotation(hasAuthority = "back:role:delete")
    @LogAnnotation(module = "删除角色")
    @ApiOperation(value = "删除角色接口", httpMethod = "POST",tags= SwggerCommonTags.SYS_ROLE_MODULE)
    @RequestMapping(value = "/deleteSysRole.do",method= RequestMethod.POST)
    public SimpleResponse deleteSysRole(
            @ApiParam(required=true, name="ids", value="角色ids 必填 多个用,号隔开 例如 1,2,3,4")
            @RequestParam(defaultValue = "") String ids
    ) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(ValidationUtils.isStrsNull(ids)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "角色id为空!");
        }
        return sysRoleService.deleteSysRole(ids);
    }

    @CacheLock(prefix="back:role:rolePermission")
    @AuthorizeAnnotation(hasAuthority = "back:role:rolePermission")
    @LogAnnotation(module = "角色分配权限")
    @ApiOperation(value = "角色分配权限接口", httpMethod = "POST",tags= SwggerCommonTags.SYS_ROLE_MODULE)
    @RequestMapping(value = "/saveSysRolePermission.do",method= RequestMethod.POST)
    public SimpleResponse saveSysRolePermission(
            @CacheParam(name="id")
            @ApiParam(required=true, name="id", value="角色id 必填 多个用,号隔开 例如 1,2,3,4")
            @RequestParam(defaultValue = "0") Long id,

            @ApiParam(required=true, name="permissionIds", value="权限permissionIds 必填 多个用,号隔开 例如 1,2,3,4")
            @RequestParam(defaultValue = "") String permissionIds
    ) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(id.equals(0L)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "角色id为空!");
        }
        if(ValidationUtils.isStrsNull(permissionIds)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "权限permissionIds为空!");
        }
        return sysRoleService.saveSysRolePermission(id,permissionIds);
    }

    @CacheLock(prefix="back:role:roleMenu")
    @AuthorizeAnnotation(hasAuthority = "back:role:roleMenu")
    @LogAnnotation(module = "角色分配菜单")
    @ApiOperation(value = "角色分配菜单接口", httpMethod = "POST",tags= SwggerCommonTags.SYS_ROLE_MODULE)
    @RequestMapping(value = "/saveRoleMenu.do",method= RequestMethod.POST)
    public SimpleResponse saveRoleMenu(
            @CacheParam(name="id")
            @ApiParam(required=true, name="id", value="角色id 必填")
            @RequestParam(defaultValue = "0") Long id,

            @ApiParam(required=true, name="menuIds", value="菜单menuIds 必填 多个用,号隔开 例如 1,2,3,4")
            @RequestParam(defaultValue = "") String menuIds
    ) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(id.equals(0L)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "角色id为空!");
        }
        if(ValidationUtils.isStrsNull(menuIds)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "菜单menuIds为空!");
        }
        return sysRoleService.saveRoleMenu(id,menuIds);
    }

}
