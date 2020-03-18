package com.cloud.log.service.impl;

import com.cloud.common.response.PageResponse;
import com.cloud.common.response.Pagetool;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.common.utils.DateUntil;
import com.cloud.log.vo.SysLogVO;
import com.cloud.model.log.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.cloud.log.dao.LogDao;
import com.cloud.log.service.LogService;
import org.springframework.util.CollectionUtils;


@Service
public class LogServiceImpl implements LogService {

	@Autowired
	private LogDao logDao;

	/**
	 * 将日志保存到数据库<br>
	 * 注解@Async是开启异步执行
	 *
	 * @param log
	 */
	@Async("taskExecutor")
	@Override
	public SimpleResponse save(SysLog log) {
		SimpleResponse resp = new SimpleResponse();
		if (log.getCreateTime() == null) {
			log.setCreateTime(DateUntil.getNowTimestamp());
		}
		if (log.getFlag() == null) {
			log.setFlag(1);
		}
		logDao.save(log);
		return resp;
	}

	@Override
	public PageResponse<SysLogVO> queryLogPage(Integer currentPage, Integer pageSize, String username, String doName,Integer flag) {
		PageResponse<SysLogVO> resp = new PageResponse<>();
		Pagetool<SysLogVO> pageList = logDao.queryLogPage(currentPage, pageSize,username,doName,flag);
		if ( CollectionUtils.isEmpty(pageList.getList()) ) {
			return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "没有数据！");
		}
		resp.setPage(pageList);
		return resp;
	}


}
