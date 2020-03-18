package com.cloud.file.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;


/**
 * @ClassName : EnableMBeanExport  //类名
 * @Description : 导入FastDFS-Client组件 //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 13:46  //时间
 */
@Configuration
@Import(FdfsClientConfig.class)
// 解决jmx重复注册bean的问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastdfsImporterConfig {
    // 导入依赖组件
}