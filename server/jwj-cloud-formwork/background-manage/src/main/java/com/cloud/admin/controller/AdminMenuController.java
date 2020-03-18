package com.cloud.admin.controller;

import com.cloud.admin.service.MenuService;
import com.cloud.admin.vo.menu.MenuVO;
import com.cloud.admin.vo.menu.UserMenuListVO;
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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : AdminMenuController  //类名
 * @Description : 系统菜单控制层  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 11:21  //时间
 */
@RequestMapping(value = "/api/admin")
@RestController
public class AdminMenuController {

    //=====================********************************************************************************//
    //=====================TODO 菜单目前只做二级菜单==========================================================//
    //=====================********************************************************************************//

    @Autowired
    private MenuService menuService;

    /**
     * 删除角色菜单关系表 系统内部调用的接口
     * @param roleIds
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "删除角色菜单关系表 系统内部调用的接口", httpMethod = "GET",tags=SwggerCommonTags.SYS_INTERNAL_API)
    @RequestMapping(value = "/internal/deleteRoleMenu.do",method= RequestMethod.POST)
    public SimpleResponse deleteRoleMenu(@RequestParam String roleIds) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(ValidationUtils.isStrsNull(roleIds)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "角色id为空!");
        }
        return menuService.deleteRoleMenu(roleIds);
    }

    /**
     * 保存角色菜单关系表  系统内部调用的接口
     * @param roleId
     * @param menuIds
     * @return
     * @throws Exception
     */
    @CacheLock(prefix="back:saveRoleMenu:add")
    @ApiOperation(value = "保存角色菜单关系表 系统内部调用的接口", httpMethod = "GET",tags=SwggerCommonTags.SYS_INTERNAL_API)
    @RequestMapping(value = "/internal/saveRoleMenu.do",method= RequestMethod.POST)
    public SimpleResponse saveRoleMenu(@CacheParam(name="roleId") @RequestParam("roleId") Long roleId,
                                         @RequestParam("menuIds")String menuIds) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(roleId==null||roleId.equals(0L)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "角色roleId为空!");
        }
        if(ValidationUtils.isStrsNull(menuIds)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "菜单menuIds为空!");
        }
        return menuService.saveRoleMenu(roleId,menuIds);
    }



    @AuthorizeAnnotation(hasAuthority = "back:menu:query")
    @ApiOperation(value = "获取菜单列表接口--(分页)", httpMethod = "POST",tags= SwggerCommonTags.SYS_MENU_MODULE)
    @RequestMapping(value = "/queryMenuPage.do",method=RequestMethod.POST)
    public PageResponse<MenuVO> queryMenuPage(
            @ApiParam(required=true, name="currentPage", value="当前页 必填")
            @RequestParam(defaultValue = "1") Integer currentPage,

            @ApiParam(required=true, name="pageSize", value="每页显示条数 默认是10条")
            @RequestParam(defaultValue = "10") Integer pageSize,

            @ApiParam(required=false, name="name", value="菜单名称 查询条件非必填")
            @RequestParam(defaultValue = "") String name
    ) throws Exception{
        return menuService.queryMenuPage(currentPage, pageSize,name);
    }

    @ApiOperation(value = "获取所有菜单接口", httpMethod = "GET",tags=SwggerCommonTags.SYS_MENU_MODULE)
    @RequestMapping(value = "/selectAllMenu.do",method=RequestMethod.GET)
    public ApiResponse<MenuVO> selectAllMenu() throws Exception{
        return menuService.selectAllMenu();
    }

    @CacheLock(prefix="back:menu:add")
    @AuthorizeAnnotation(hasAuthority = "back:menu:add")
    @LogAnnotation(module = "添加菜单")
    @ApiOperation(value = "添加菜单接口", httpMethod = "POST",tags= SwggerCommonTags.SYS_MENU_MODULE)
    @RequestMapping(value = "/addMenu.do",method= RequestMethod.POST)
    public SimpleResponse addMenu(
            @CacheParam(name="name")
            @ApiParam(required=true, name="name", value="菜单名称 必填")
            @RequestParam(defaultValue = "") String name,

            @ApiParam(required=true, name="parentId", value="父菜单id 默认值0表示一级菜单")
            @RequestParam(defaultValue = "0") Long parentId,

            @ApiParam(required=false, name="url", value="菜单url 必填")
            @RequestParam(defaultValue = "") String url,

            @ApiParam(required=true, name="css", value="菜单样式 必填")
            @RequestParam(defaultValue = "") String css,

            @ApiParam(required=true, name="sort", value="排序 必填 越小越在前面")
            @RequestParam(defaultValue = "") Integer sort
    ) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(ValidationUtils.isStrsNull(name,css)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "参数传入错误!");
        }
        if(sort==null){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "sort为空!");
        }
        return menuService.addMenu(parentId,name,url,css,sort);
    }

    @AuthorizeAnnotation(hasAuthority = "back:menu:delete")
    @LogAnnotation(module = "删除菜单")
    @ApiOperation(value = "删除菜单接口", httpMethod = "POST",tags= SwggerCommonTags.SYS_MENU_MODULE)
    @RequestMapping(value = "/deleteMenu.do",method= RequestMethod.POST)
    public SimpleResponse deleteMenu(
            @ApiParam(required=true, name="id", value="菜单id 必填")
            @RequestParam(defaultValue = "") Long id
    ) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(id==null){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "菜单id为空!");
        }
        return menuService.deleteMenu(id);
    }

    @ApiOperation(value = "获取用户菜单接口", httpMethod = "GET",tags=SwggerCommonTags.SYS_MENU_MODULE)
    @RequestMapping(value = "/selectUserMenu.do",method=RequestMethod.GET)
    public ApiResponse<UserMenuListVO> selectUserMenu() throws Exception{
        return menuService.selectUserMenu();
    }


}
