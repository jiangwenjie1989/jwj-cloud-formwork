package com.cloud.admin.dao;

import com.cloud.admin.base.BaseDao;
import com.cloud.admin.repository.MenuRepository;
import com.cloud.admin.vo.menu.MenuVO;
import com.cloud.common.response.Pagetool;
import com.cloud.common.utils.ValidationUtils;
import com.cloud.model.manage.Menu;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName : MenuDao  //类名
 * @Description : 菜单Dao  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 11:19  //时间
 */
@Repository
public class MenuDao extends BaseDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    private MenuRepository menuRepository;


    public Pagetool<MenuVO> queryMenuPage(Integer currentPage, Integer pageSize, String name) {

        Pagetool<MenuVO> page = new Pagetool<>();
        page.setPageNumber(currentPage - 1);
        page.setPageSize(pageSize);
        page.setCurrentPageCount(currentPage);
        //参数
        List<Object> params = Lists.newArrayList();

        StringBuffer sql = new StringBuffer();
        sql.append("  SELECT m.id AS id, m.parent_id AS parentId,m.`name` AS `name`,m.css AS css,m.update_time AS createTime,m.url AS url,m.sort AS sort ");
        sql.append("  FROM menu m WHERE 1=1 ");


        if (!ValidationUtils.StrisNull(name)) {
            sql.append(" and m.`name` like '%"+name+"%'");
        }
        sql.append(" ORDER BY m.create_time DESC ");
        pageForBean(jdbcTemplate, sql.toString(), page, MenuVO.class,params.toArray());
        return page;
    }

    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    public List<Menu> findByParentId(Long parentId) {
        return menuRepository.findByParentId(parentId);
    }

    public void save(Menu menu) {
        menuRepository.save(menu);
    }

    public Optional<Menu> findById(Long id) {
        return menuRepository.findById(id);
    }

    public void deleteById(Long id) {
        menuRepository.deleteById(id);
    }

    public void deleteByParentId(Long id) {
        menuRepository.deleteByParentId(id);
    }

    public void deleteAll(List<Menu> menuList) {
        menuRepository.deleteAll(menuList);
    }


    public List<Menu> findByIds(List<Long> menuIdList) {
        return menuRepository.findByIds(menuIdList);
    }
}
