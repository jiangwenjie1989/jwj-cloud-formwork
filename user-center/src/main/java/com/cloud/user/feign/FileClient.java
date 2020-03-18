package com.cloud.user.feign;

import com.cloud.common.response.ApiResponse;
import com.cloud.model.file.FileDTO;
import com.cloud.user.feign.fallback.FileClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "file-center",fallbackFactory = FileClientFallback.class)
public interface FileClient {

    @PostMapping(value = "/file/upload/external/creatEqrCode.do")
    public ApiResponse<FileDTO> creatEqrCode(@RequestParam("content") String content);



}
