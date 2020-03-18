package com.cloud.log.dao;


import com.cloud.common.response.Pagetool;
import com.cloud.common.utils.ValidationUtils;
import com.cloud.log.base.BaseDao;
import com.cloud.log.repository.LogRepository;
import com.cloud.log.vo.SysLogVO;
import com.cloud.model.log.SysLog;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LogDao extends BaseDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private LogRepository logRepository;

	public void save(SysLog log) {
		logRepository.save(log);
	}


	public Pagetool<SysLogVO> queryLogPage(Integer currentPage, Integer pageSize, String username, String doName, Integer flag) {
		Pagetool<SysLogVO> page = new Pagetool<>();
		page.setPageNumber(currentPage - 1);
		page.setPageSize(pageSize);
		page.setCurrentPageCount(currentPage);
		//参数
		List<Object> params = Lists.newArrayList();

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT l.username AS username,l.create_time AS createTime,l.do_name AS doName,l.flag AS flag,l.remark AS remark ,l.params AS params ");
		sql.append(" FROM sys_log l WHERE 1=1 ");

		if (!ValidationUtils.StrisNull(username)) {
			sql.append(" and l.`username` like '%"+username+"%'");
		}
		if (!ValidationUtils.StrisNull(username)) {
			sql.append(" and l.doName like '%"+doName+"%'");
		}
		if (!flag.equals(2L)) {
			sql.append(" and l.`flag` =?");
			params.add(flag);
		}
		sql.append(" ORDER BY l.create_time DESC ");
		pageForBean(jdbcTemplate, sql.toString(), page, SysLogVO.class,params.toArray());
		return page;
	}
}
