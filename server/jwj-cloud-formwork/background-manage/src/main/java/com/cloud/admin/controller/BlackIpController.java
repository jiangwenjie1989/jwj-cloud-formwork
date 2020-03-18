package com.cloud.admin.controller;

import com.cloud.admin.service.BlackIpService;
import com.cloud.admin.vo.blackip.BlackIpVO;
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
import com.cloud.model.manage.BlackIpDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName : BlackIpController  //类名
 * @Description : 系统黑名ip单控制层  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 11:21  //时间
 */
@RequestMapping(value = "/api/admin")
@RestController
public class BlackIpController {

    @Autowired
    private BlackIpService blackIpService;



    @ApiOperation(value = "获取所有ip黑名单接口 系统内部调用的接口", httpMethod = "GET",tags=SwggerCommonTags.SYS_INTERNAL_API)
    @GetMapping(value = "/internal/selectAllBlackIp.do")
    public ApiResponse<BlackIpDTO> selectAllBlackIp( ) throws Exception{
        return blackIpService.selectAllBlackIp();
    }


    @AuthorizeAnnotation(hasAuthority = "back:blackIp:query")
    @ApiOperation(value = "获取黑名单ip列表接口--(分页)", httpMethod = "POST",tags= SwggerCommonTags.SYS_BLACKIP_MODULE)
    @RequestMapping(value = "/queryBlackIpPage.do",method=RequestMethod.POST)
    public PageResponse<BlackIpVO> queryBlackIpPage(
            @ApiParam(required=true, name="currentPage", value="当前页 必填")
            @RequestParam(defaultValue = "1") Integer currentPage,

            @ApiParam(required=true, name="pageSize", value="每页显示条数 默认是10条")
            @RequestParam(defaultValue = "10") Integer pageSize

    ) throws Exception{
        return blackIpService.queryBlackIpPage(currentPage, pageSize);
    }

    @CacheLock(prefix="back:blackIp:add")
    @AuthorizeAnnotation(hasAuthority = "back:blackIp:add")
    @LogAnnotation(module = "添加ip黑名单")
    @ApiOperation(value = "添加ip黑名单接口", httpMethod = "POST",tags= SwggerCommonTags.SYS_BLACKIP_MODULE)
    @RequestMapping(value = "/addBlackIp.do",method= RequestMethod.POST)
    public SimpleResponse addBlackIp(
            @CacheParam(name="ipAddress")
            @ApiParam(required=true, name="ipAddress", value="ip地址 必填")
            @RequestParam(defaultValue = "") String ipAddress
    ) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(ValidationUtils.isStrsNull(ipAddress)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "参数传入错误!");
        }
        return blackIpService.addBlackIp(ipAddress);
    }


    @AuthorizeAnnotation(hasAuthority = "back:blackIp:delete")
    @LogAnnotation(module = "删除ip黑名单")
    @ApiOperation(value = "删除ip黑名单接口", httpMethod = "GET",tags= SwggerCommonTags.SYS_BLACKIP_MODULE)
    @RequestMapping(value = "/deleteBlackIp.do",method= RequestMethod.GET)
    public SimpleResponse deleteBlackIp(
            @ApiParam(required=true, name="id", value="黑名单id 必填")
            @RequestParam(defaultValue = "") Long id
    ) throws Exception {
        SimpleResponse resp=new SimpleResponse();
        if(id==null){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "菜单id为空!");
        }
        return blackIpService.deleteBlackIp(id);
    }

}
