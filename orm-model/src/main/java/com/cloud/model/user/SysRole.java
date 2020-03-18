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
 * 角色表(sys_role)
 * 
 * @author jiangwenjie
 * @version 1.0.0 2020-03-05
 */
@Data
@Entity
@Table(name = "sys_role")
public class SysRole implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = -59554528556788053L;

    /* This code was generated by TableGo tools, mark 1 begin. */

    /** id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 19)
    private Long id;

    /** 角色code */
    @Column(name = "code", unique = true, nullable = true, length = 25)
    private String code;

    /** 角色名称 */
    @Column(name = "name", unique = true, nullable = true, length = 60)
    private String name;

    /** 创建时间 */
    @Column(name = "create_time", nullable = true, length = 19)
    private Timestamp createTime;

    /** 修改时间 */
    @Column(name = "update_time", nullable = true, length = 19)
    private Timestamp updateTime;


}