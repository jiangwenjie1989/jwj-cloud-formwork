package com.cloud.gateway.feign;


import com.cloud.common.response.ApiResponse;
import com.cloud.gateway.feign.fallback.ManageClientFallback;
import com.cloud.model.manage.BlackIpDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "background-manage",fallbackFactory = ManageClientFallback.class)
public interface ManageClient {

    @GetMapping(value = "/admin/internal/selectAllBlackIp.do")
    public ApiResponse<BlackIpDTO> selectAllBlackIp();



}
