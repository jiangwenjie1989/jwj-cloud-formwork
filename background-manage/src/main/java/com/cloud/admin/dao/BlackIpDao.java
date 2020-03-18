package com.cloud.admin.dao;

import com.cloud.admin.base.BaseDao;
import com.cloud.admin.repository.BlackIpRepository;
import com.cloud.admin.vo.blackip.BlackIpVO;
import com.cloud.common.response.Pagetool;
import com.cloud.model.manage.BlackIp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName : BlackIpDao  //类名
 * @Description : 黑名单ip Dao  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 11:19  //时间
 */
@Repository
public class BlackIpDao extends BaseDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    private BlackIpRepository blackIpRepository;


    public Pagetool<BlackIpVO> queryBlackIpPage(Integer currentPage, Integer pageSize) {

        Pagetool<BlackIpVO> page = new Pagetool<>();
        page.setPageNumber(currentPage - 1);
        page.setPageSize(pageSize);
        page.setCurrentPageCount(currentPage);
        StringBuffer sql = new StringBuffer();
        sql.append("  SELECT b.id AS id, b.ip_address AS ipAddress ,b.create_time AS createTime FROM black_ip b ");
        sql.append(" ORDER BY b.create_time DESC ");
        pageForBean(jdbcTemplate, sql.toString(), page, BlackIpVO.class);
        return page;
    }


    public Optional<BlackIp> findByIpAddress(String ipAddress) {
        return  blackIpRepository.findByIpAddress(ipAddress);
    }

    public void save(BlackIp blackIp) {
        blackIpRepository.save(blackIp);
    }

    public Optional<BlackIp> findById(Long id) {
        return blackIpRepository.findById(id);
    }

    public void deleteById(Long id) {
        blackIpRepository.deleteById(id);
    }

    public List<BlackIp> findAll() {
        return blackIpRepository.findAll();
    }
}
