package com.cloud.model.manage;/*
 * Welcome to use the TableGo Tools.
 * 
 * http://www.tablego.cn
 * 
 * http://vipbooks.iteye.com
 * http://blog.csdn.net/vipbooks
 * http://www.cnblogs.com/vipbooks
 * 
 * Author: bianj
 * Email: tablego@qq.com
 * Version: 6.6.6
 */
import lombok.Data;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 菜单表(menu)
 * 
 * @author jiangwenjie
 * @version 1.0.0 2020-03-05
 */
@Data
@Entity
@Table(name = "menu")
public class Menu implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 4290846401417890991L;

    /* This code was generated by TableGo tools, mark 1 begin. */

    /** id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 19)
    private Long id;

    /** 父菜单id */
    @Column(name = "parent_id", nullable = true, length = 19)
    private Long parentId;

    /** 菜单名称 */
    @Column(name = "name", nullable = true, length = 50)
    private String name;

    /** 菜单url */
    @Column(name = "url", nullable = true, length = 100)
    private String url;

    /** 菜单样式 */
    @Column(name = "css", nullable = true, length = 80)
    private String css;

    /** 排序 */
    @Column(name = "sort", nullable = true, length = 10)
    private Integer sort;

    /** 创建时间 */
    @Column(name = "create_time", nullable = true, length = 19)
    private Timestamp createTime;

    /** 修改时间 */
    @Column(name = "update_time", nullable = true, length = 19)
    private Timestamp updateTime;


}