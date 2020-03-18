package com.cloud.file.controller;



import com.cloud.common.constants.SwggerCommonTags;
import com.cloud.common.response.ApiResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.common.utils.QRCodeUtils;
import com.cloud.common.utils.ToolRandoms;
import com.cloud.common.utils.ValidationUtils;
import com.cloud.file.from.FileFrom;
import com.cloud.file.utils.FastDFSClient;
import com.cloud.file.utils.FileUtils;
import com.cloud.file.vo.FileVO;
import com.cloud.model.file.FileDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName : SysFileController  //类名
 * @Description : 文件中心 //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 13:46  //时间
 */
@RequestMapping(value = "/file/upload")
@RestController
public class SysFileController {

    @Autowired
    private FastDFSClient fastDFSClient;

    @Value("${fdfs.groupName}")
    private String groupName;

    @Value("${fdfs.tempDirectory}")
    private String tempDirectory;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @ApiOperation(value = "文件上传Base64数据接口 json接收参数 --图片文件,返回原图片url 和压缩后的图片url", httpMethod = "POST",tags= SwggerCommonTags.SYS_FILE_MODULE)
    @RequestMapping(value = "/external/uploadFileBase64JSON.do",method= RequestMethod.POST)
    public ApiResponse<FileVO> uploadFileBase64( @RequestBody FileFrom fileFrom) throws Exception {
        ApiResponse<FileVO> resp=new ApiResponse<>();
        String fileBase64Data = fileFrom.getFileBase64Data();

        if(ValidationUtils.StrisNull(fileBase64Data)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "fileBase64Data数据为空!");
        }
        String userFacePath = tempDirectory + ToolRandoms.getUuid(true) + "userface64.png";
        FileUtils.base64ToFile(userFacePath, fileBase64Data);

        // 上传文件到fastdfs
        MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);
        String url = fastDFSClient.uploadBase64(faceFile);
        // 获取缩略图的url
        String thump = "_80x80.";
        String arr[] = url.split("\\.");
        String thumpImgUrl = groupName+arr[0] + thump + arr[1];

        String originalImage=groupName+arr[0]+"."+arr[1];

        FileVO fileVO=new FileVO();
        fileVO.setOriginalImage(originalImage);
        fileVO.setThumbImage(thumpImgUrl);
        resp.setData(fileVO);
        return resp;
    }



    @ApiOperation(value = "文件对象上传接口--图片文件,返回原图片url 和压缩后的图片url", httpMethod = "POST",tags= SwggerCommonTags.SYS_FILE_MODULE)
    @RequestMapping(value = "/external/uploadFile.do",method= RequestMethod.POST)
    public ApiResponse<FileVO> uploadFile(
            @ApiParam(required=true, name="file", value="文件对象 必填")
            @RequestParam(defaultValue = "") MultipartFile file
    ) throws Exception {
        ApiResponse<FileVO> resp=new ApiResponse<>();
        String url = fastDFSClient.uploadBase64(file);
        // 获取缩略图的url
        String thump = "_80x80.";
        String arr[] = url.split("\\.");
        String thumpImgUrl = groupName+arr[0] + thump + arr[1];

        String originalImage=groupName+arr[0]+"."+arr[1];

        FileVO fileVO=new FileVO();
        fileVO.setOriginalImage(originalImage);
        fileVO.setThumbImage(thumpImgUrl);
        resp.setData(fileVO);
        return resp;
    }

    @ApiOperation(value = "生成二维码接口", httpMethod = "POST",tags= SwggerCommonTags.SYS_FILE_MODULE)
    @RequestMapping(value = "/external/creatEqrCode.do",method= RequestMethod.POST)
    public ApiResponse<FileDTO> creatEqrCode(
            @ApiParam(required=true, name="content", value="二维码内容 必填")
            @RequestParam (defaultValue = "")  String content
    ) throws Exception {
        ApiResponse<FileDTO> resp=new ApiResponse<>();
        if(ValidationUtils.isStrsNull(content)){
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "二维码内容为空!");
        }
        String qrCodePath = tempDirectory + ToolRandoms.getUuid(true) + "qrcode.png";
        qrCodeUtils.createQRCode(qrCodePath, content);
        MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);
        String qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
        FileDTO fileDTO=new FileDTO();
        fileDTO.setOriginalImage(groupName+qrCodeUrl);
        resp.setData(fileDTO);
        return resp;
    }

    @ApiOperation(value = "大文件对象上传接口--返回原数据url 大一点的文件系统控制500兆一下", httpMethod = "POST",tags= SwggerCommonTags.SYS_FILE_MODULE)
    @RequestMapping(value = "/external/uploadFileBigData.do",method= RequestMethod.POST)
    public ApiResponse<FileVO> uploadFileBigData(
            @ApiParam(required=true, name="file", value="文件对象 必填")
            @RequestParam(defaultValue = "") MultipartFile file
    ) throws Exception {
        ApiResponse<FileVO> resp=new ApiResponse<>();
        String url = fastDFSClient.uploadFile(file);
        String originalImage=groupName+url;
        FileVO fileVO=new FileVO();
        fileVO.setOriginalImage(originalImage);
        resp.setData(fileVO);
        return resp;
    }

}
