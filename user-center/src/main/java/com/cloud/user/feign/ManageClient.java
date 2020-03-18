package com.cloud.user.feign;

import com.cloud.common.response.SimpleResponse;
import com.cloud.user.feign.fallback.ManageClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "background-manage",fallbackFactory = ManageClientFallback.class)
public interface ManageClient {

    @PostMapping(value = "/admin/internal/deleteRoleMenu.do")
    public SimpleResponse deleteRoleMenu(@RequestParam("roleIds") String roleIds);

    @PostMapping(value = "/admin/internal/saveRoleMenu.do")
    public SimpleResponse saveRoleMenu(@RequestParam("roleId")Long roleId, @RequestParam("menuIds")String menuIds);

}
