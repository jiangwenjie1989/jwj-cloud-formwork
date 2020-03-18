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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色菜单关系表(role_menu)
 * 
 * @author jiangwenjie
 * @version 1.0.0 2020-03-05
 */
@Data
@Entity
@Table(name = "role_menu")
public class RoleMenu implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 5761479875332106L;

    /* This code was generated by TableGo tools, mark 1 begin. */

    /** id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 19)
    private Long id;

    /** 角色id */
    @Column(name = "sys_role_id", nullable = true, length = 19)
    private Long sysRoleId;

    /** 菜单id */
    @Column(name = "menu_id", nullable = true, length = 19)
    private Long menuId;


}