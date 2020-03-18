package com.cloud.model.user;/*
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
 * 系统权限表(sys_permission)
 * 
 * @author jiangwenjie
 * @version 1.0.0 2020-03-05
 */
@Data
@Entity
@Table(name = "sys_permission")
public class SysPermission implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 4347385192542421046L;

    /* This code was generated by TableGo tools, mark 1 begin. */

    /** id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 19)
    private Long id;

    /** 权限标识 */
    @Column(name = "permission", nullable = true, length = 80)
    private String permission;

    /** 权限名称 */
    @Column(name = "name", nullable = true, length = 60)
    private String name;

    /** 创建时间 */
    @Column(name = "create_time", nullable = true, length = 19)
    private Timestamp createTime;

    /** 修改时间 */
    @Column(name = "update_time", nullable = true, length = 19)
    private Timestamp updateTime;


}