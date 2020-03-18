package com.cloud.log.controller;

import com.cloud.common.annotation.AuthorizeAnnotation;
import com.cloud.common.constants.SwggerCommonTags;
import com.cloud.common.response.PageResponse;
import com.cloud.common.response.SimpleResponse;
import com.cloud.log.service.LogService;
import com.cloud.log.vo.SysLogVO;
import com.cloud.model.log.SysLog;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/admin")
public class LogController {

	@Autowired
	private LogService logService;


	/**
	 * 系统内部调用
	 * @param sysLog
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "保存日志接口 系统内部调用的接口", httpMethod = "GET",tags=SwggerCommonTags.SYS_INTERNAL_API)
	@RequestMapping(value = "/internal/saveSysLog.do",method=RequestMethod.POST)
	public SimpleResponse saveSysLog(@RequestBody SysLog sysLog) throws Exception{
		return logService.save(sysLog);
	}

	@AuthorizeAnnotation(hasAuthority = "back:log:query")
	@ApiOperation(value = "获取系统日志列表接口--(分页)", httpMethod = "POST",tags= SwggerCommonTags.SYS_LOG_MODULE)
	@RequestMapping(value = "/queryLogPage.do",method=RequestMethod.POST)
	public PageResponse<SysLogVO> queryLogPage(
			@ApiParam(required=true, name="currentPage", value="当前页 必填")
			@RequestParam(defaultValue = "1") Integer currentPage,

			@ApiParam(required=true, name="pageSize", value="每页显示条数 默认是10条")
			@RequestParam(defaultValue = "10") Integer pageSize,

			@ApiParam(required=false, name="username", value="登录系统用户名 查询条件非必填")
			@RequestParam(defaultValue = "") String username,

			@ApiParam(required=false, name="doName", value="操作名称 查询条件非必填")
			@RequestParam(defaultValue = "") String doName,

			@ApiParam(required=false, name="flag", value="是否成功 1表示成功 0表示失败 2全部 查询条件非必填")
			@RequestParam(defaultValue = "2") Integer flag
	) throws Exception{
		return logService.queryLogPage(currentPage, pageSize,username,doName,flag);
	}




}
