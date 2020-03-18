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
import com.cloud.user.service.SysUserService;
import com.cloud.user.vo.user.SysUserInfoListVO;
import com.cloud.user.vo.user.SysUserInfoVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName : SysUserController  //类名
 * @Description : 系统用户控制层 //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-05 13:46  //时间
 */
@RequestMapping(value = "/api/admin")
@RestController
public class SysUserController {


    @Autowired
    private SysUserService sysUserService;

    /**
     * 添加或者修改系统用户接口  我这里和前端约定好做的都是form提交 ，也可以用json提交，就需要用对象接收参数并添加注解@RequestBody
     * @param phone
     * @param password
     * @param username
     * @param nickname
     * @param headImgUrl
     * @return
     * @throws Exception
     */
    @CacheLock(prefix="back:user:add")
    @AuthorizeAnnotation(hasAuthority = "back:user:add")
    @LogAnnotation(module = "添加系统用户")
    @ApiOperation(value = "添加系统用户接口", httpMethod = "POST",tags= SwggerCommonTags.SYS_USER_MODULE)
    @RequestMapping(value = "/addSysUser.do",method= RequestMethod.POST)
    public SimpleResponse addSysUser(

            @CacheParam(name="phone")
            @ApiParam(required=true, name="phone", value="手机号 必填")
            @RequestParam(defaultValue = "") String phone,

            @CacheParam(name="password")
            @ApiParam(required=true, name="password", value="密码 必填")
            @RequestParam(defaultValue = "") String password,

            @CacheParam(name="username")
            @ApiParam(required=true, name="username", value="用户名 必填")
            @RequestParam(defaultValue = "") String username,

            @ApiParam(required=false, name="nickname", value="昵称 非必填")
            @RequestParam(defaultValue = "") String nickname,

            @ApiParam(required=false, name="headImgUrl", value="用户头像url 非必填")
            @RequestParam(defaultValue = "") String headImgUrl
    ) throws Exception {

        SimpleResponse resp=new SimpleResponse();
        if(ValidationUtils.isStrsNull(phone,password,username)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "参数传入错误!");
        }
        return sysUserService.addSysUser(phone,password,username,nickname,headImgUrl);
    }

    /**
     * @param userId
     * @param password
     * @param nickname
     * @param headImgUrl
     * @return
     * @throws Exception
     */
    @AuthorizeAnnotation(hasAuthority = "back:user:update")
    @LogAnnotation(module = "修改系统用户信息")
    @ApiOperation(value = "修改系统用户接口", httpMethod = "POST",tags= SwggerCommonTags.SYS_USER_MODULE)
    @RequestMapping(value = "/updateSysUser.do",method= RequestMethod.POST)
    public SimpleResponse addOrUpdateSysUser(
            @ApiParam(required=true, name="userId", value="用户id 添加的时候不需要传默认值0 修改必填")
            @RequestParam(defaultValue = "0") Long userId,

            @ApiParam(required=false, name="password", value="密码 非必填")
            @RequestParam(defaultValue = "") String password,

            @ApiParam(required=false, name="nickname", value="昵称 非必填")
            @RequestParam(defaultValue = "") String nickname,

            @ApiParam(required=false, name="headImgUrl", value="用户头像url 非必填")
            @RequestParam(defaultValue = "") String headImgUrl
    ) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(userId.equals(0L)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "用户id必填!");
        }
        return sysUserService.updateSysUser(userId,password,nickname,headImgUrl);
    }

    @AuthorizeAnnotation(hasAuthority = "back:user:query")
    @ApiOperation(value = "获取后台管理用户信息列表接口--(分页)", httpMethod = "POST",tags=SwggerCommonTags.SYS_USER_MODULE)
    @RequestMapping(value = "/querySysUserPage.do",method=RequestMethod.POST)
    public PageResponse<SysUserInfoListVO> querySysUserPage(
            @ApiParam(required=true, name="currentPage", value="当前页 必填")
            @RequestParam(defaultValue = "1") Integer currentPage,

            @ApiParam(required=true, name="pageSize", value="每页显示条数 默认是10条")
            @RequestParam(defaultValue = "10") Integer pageSize,

            @ApiParam(required=false, name="phone", value="手机号 查询条件非必填")
            @RequestParam(defaultValue = "") String phone,

            @ApiParam(required=false, name="username", value="用户名 查询条件非必填")
            @RequestParam(defaultValue = "") String username,

            @ApiParam(required=false, name="nickname", value="昵称 查询条件非必填")
            @RequestParam(defaultValue = "") String nickname,

            @ApiParam(required=false, name="status", value="状态 1表示有效 0 表示无效 2表示全部 查询条件非必填")
            @RequestParam(defaultValue = "2") Integer status
    ) throws Exception{
        return sysUserService.querySysUserPage(currentPage, pageSize,phone,username,nickname,status);
    }


    @ApiOperation(value = "后台管理用户登录接口", httpMethod = "POST",tags= SwggerCommonTags.SYS_USER_MODULE)
    @RequestMapping(value = "/external/sysUserLogin.do",method= RequestMethod.POST)
    public ApiResponse<SysUserInfoVO> sysUserLogin(

            @ApiParam(required=true, name="loginAccount", value="登录账号 必填")
            @RequestParam(defaultValue = "") String loginAccount,

            @ApiParam(required=true, name="password", value="密码 必填")
            @RequestParam(defaultValue = "") String password
    ) throws Exception {
        ApiResponse<SysUserInfoVO> resp=new ApiResponse<>();
        if(ValidationUtils.isStrsNull(loginAccount,password)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "参数传入错误!");
        }
        return sysUserService.sysUserLogin(loginAccount,password);
    }

    @ApiOperation(value = "获取用户信息接口--通过token", httpMethod = "GET",tags= SwggerCommonTags.SYS_USER_MODULE)
    @RequestMapping(value = "/getSysUserInfo.do",method= RequestMethod.GET)
    public ApiResponse<SysUserInfoVO> getSysUserInfo() throws Exception {
        return sysUserService.getSysUserInfo();
    }

    @AuthorizeAnnotation(hasAuthority = "back:user:userRole")
    @LogAnnotation(module = "用户分配角色")
    @ApiOperation(value = "用户分配角色接口", httpMethod = "POST",tags= SwggerCommonTags.SYS_USER_MODULE)
    @RequestMapping(value = "/saveUserRole.do",method= RequestMethod.POST)
    public SimpleResponse saveRoleMenu(
            @ApiParam(required=true, name="userId", value="用户useId 必填")
            @RequestParam(defaultValue = "0") Long userId,

            @ApiParam(required=true, name="roleIds", value="角色roleIds 必填 多个用,号隔开 例如 1,2,3,4")
            @RequestParam(defaultValue = "") String roleIds
    ) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(userId.equals(0L)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "用户useId为空!");
        }
        if(ValidationUtils.isStrsNull(roleIds)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "角色roleIds 为空!");
        }
        return sysUserService.saveUserRole(userId,roleIds);
    }


}
