package com.cloud.admin.repository;

import com.cloud.model.manage.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByParentId(Long parentId);

    @Modifying
    @Query(value="DELETE FROM  menu  WHERE parent_id IN (?1) ", nativeQuery=true)
    void deleteByParentId(Long id);

    @Query(value="SELECT * FROM  menu  WHERE id IN (?1) ", nativeQuery=true)
    List<Menu> findByIds(List<Long> menuIdList);
}
