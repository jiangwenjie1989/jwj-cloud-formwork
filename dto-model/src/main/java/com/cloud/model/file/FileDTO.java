package com.cloud.model.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName : FileDTO  //类名
 * @Description : 文件DTO  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-10 17:14  //时间
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO implements Serializable {

    private String originalImage;


}
