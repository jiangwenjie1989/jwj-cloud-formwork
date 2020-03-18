package com.cloud.file.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName : FileVO  //类名
 * @Description : 文件VO  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-09 23:01  //时间
 */
@Data
public class FileVO {

    @ApiModelProperty(value="原图访问url地址")
    private String originalImage;

    @ApiModelProperty(value="缩略图访问url地址")
    private String thumbImage;

}
