package com.cloud.user.controller;

import com.cloud.common.annotation.CacheLock;
import com.cloud.common.annotation.CacheParam;
import com.cloud.common.constants.SwggerCommonTags;
import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.common.utils.ValidationUtils;
import com.cloud.user.service.AppUserService;
import com.cloud.user.vo.user.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : AppUserController  //类名
 * @Description : 用户控制层 //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-10 13:46  //时间
 */
@RequestMapping(value = "/api/app")
@RestController
public class AppUserController {

    @Autowired
    private AppUserService appUserService;



    @CacheLock(prefix="app:user:registerOrLogin")
    @ApiOperation(value = "app用户注册或者登录接口", httpMethod = "POST",tags= SwggerCommonTags.APP_USER_MODULE)
    @RequestMapping(value = "/external/registerOrLogin.do",method= RequestMethod.POST)
    public ApiResponse<AppUserVO> registerOrLogin(
            @CacheParam(name="phone")
            @ApiParam(required=true, name="phone", value="手机号 必填")
            @RequestParam(defaultValue = "") String phone,

            @CacheParam(name="password")
            @ApiParam(required=true, name="password", value="密码 必填")
            @RequestParam(defaultValue = "") String password,

            @CacheParam(name="cid")
            @ApiParam(required=true, name="cid", value="设备唯一id 必填")
            @RequestParam(defaultValue = "") String cid
    ) throws Exception {
        ApiResponse<AppUserVO> resp=new ApiResponse<>();
        if(ValidationUtils.isStrsNull(phone,password,cid)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "参数传入错误!");
        }
        return appUserService.registerOrLogin(phone,password,cid);
    }


    @ApiOperation(value = "修改用户信息接口", httpMethod = "POST",tags= SwggerCommonTags.APP_USER_MODULE)
    @RequestMapping(value = "/updateAppUser.do",method= RequestMethod.POST)
    public ApiResponse<AppUserVO> updateAppUser(
            @ApiParam(required=true, name="nickname", value="用户昵称 选填")
            @RequestParam(defaultValue = "") String nickname,

            @ApiParam(required=true, name="faceImage", value="缩略图 选填")
            @RequestParam(defaultValue = "") String faceImage,

            @ApiParam(required=true, name="faceImageBig", value="原图图 选填")
            @RequestParam(defaultValue = "") String faceImageBig

    ) throws Exception {
        return appUserService.updateAppUser(nickname,faceImage,faceImageBig);
    }

    @ApiOperation(value = "搜索好友接口", httpMethod = "POST",tags= SwggerCommonTags.APP_USER_MODULE)
    @RequestMapping(value = "/searchUser.do",method= RequestMethod.POST)
    public ApiResponse<AppUserVO> searchUser(
            @ApiParam(required=true, name="friendUsername", value="用户名称 必填")
            @RequestParam(defaultValue = "") String friendUsername
    ) throws Exception {
        ApiResponse<AppUserVO> resp=new ApiResponse<>();
        if(ValidationUtils.isStrsNull(friendUsername)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "参数传入错误!");
        }
        return appUserService.searchUser(friendUsername);
    }

    @CacheLock(prefix="app:user:addFriend")
    @ApiOperation(value = "发送添加好友的请求接口", httpMethod = "POST",tags= SwggerCommonTags.APP_USER_MODULE)
    @RequestMapping(value = "/addFriendRequest.do",method= RequestMethod.POST)
    public SimpleResponse addFriendRequest(
            @CacheParam(name="friendUsername")
            @ApiParam(required=true, name="friendUsername", value="用户名称 必填")
            @RequestParam(defaultValue = "") String friendUsername
    ) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(ValidationUtils.isStrsNull(friendUsername)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "参数传入错误!");
        }
        return appUserService.addFriendRequest(friendUsername);
    }

    @ApiOperation(value = "查询所有发送添加好友请求接口", httpMethod = "GET",tags= SwggerCommonTags.APP_USER_MODULE)
    @RequestMapping(value = "/queryFriendRequests.do",method= RequestMethod.GET)
    public ApiResponse<FriendRequestVO> queryFriendRequests() throws Exception {
        return appUserService.queryFriendRequests();
    }

    @ApiOperation(value = "查询我的好友列表接口", httpMethod = "GET",tags= SwggerCommonTags.APP_USER_MODULE)
    @RequestMapping(value = "/queryMyFriends.do",method= RequestMethod.GET)
    public ApiResponse<MyFriendsVO> queryMyFriends() throws Exception {
        return appUserService.queryMyFriends();
    }


    @CacheLock(prefix="app:user:aof")
    @ApiOperation(value = "同意或者忽略添加好友请求接口", httpMethod = "POST",tags= SwggerCommonTags.APP_USER_MODULE)
    @RequestMapping(value = "/agreeOrIgnoreFriends.do",method= RequestMethod.POST)
    public ApiResponse<MyFriendsVO> agreeOrIgnoreFriends(
            @CacheParam(name="sendUserId")
            @ApiParam(required=true, name="sendUserId", value="发送方id  必填")
            @RequestParam(defaultValue = "0") Long sendUserId,

            @CacheParam(name="type")
            @ApiParam(required=true, name="type", value="操作类型 0忽略 1通过  必填")
            @RequestParam(defaultValue = "2")  Integer type
    ) throws Exception {
        ApiResponse<MyFriendsVO> resp=new ApiResponse<>();
        if(sendUserId.equals(0L)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "发送方id为空!");
        }
        if(type.equals(2)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "操作类型为空!");
        }
        return appUserService.agreeOrIgnoreFriends(sendUserId,type);
    }

}
