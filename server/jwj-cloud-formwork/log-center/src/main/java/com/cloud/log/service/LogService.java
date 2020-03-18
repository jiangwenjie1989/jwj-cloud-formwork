package com.cloud.log.service;


import com.cloud.common.response.PageResponse;
import com.cloud.common.response.SimpleResponse;
import com.cloud.log.vo.SysLogVO;
import com.cloud.model.log.SysLog;

public interface LogService {

	SimpleResponse save(SysLog log);

    PageResponse<SysLogVO> queryLogPage(Integer currentPage, Integer pageSize, String username, String doName,Integer flag);
}
