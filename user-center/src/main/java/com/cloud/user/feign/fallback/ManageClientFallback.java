package com.cloud.user.feign.fallback;

import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.user.feign.ManageClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName : ManageClientFallback  //类名
 * @Description : 失败降级  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-07 12:42  //时间
 */
@Component
public class ManageClientFallback implements FallbackFactory<ManageClient> {
    @Override
    public ManageClient create(Throwable e) {
        return new ManageClient() {
            @Override
            public SimpleResponse deleteRoleMenu(String roleIds) {
                e.printStackTrace();
                SimpleResponse resp = new SimpleResponse();
                return resp.setReturnErrMsg(resp, HttpCodeE.调用本地服务失败.value,
                        SysRespStatusE.失败.getDesc(), "调用本地服务失败！ManageClient.deleteRoleMenu");
            }

            @Override
            public SimpleResponse saveRoleMenu(Long roleId, String menuIds) {
                e.printStackTrace();
                SimpleResponse resp = new SimpleResponse();
                return resp.setReturnErrMsg(resp, HttpCodeE.调用本地服务失败.value,
                        SysRespStatusE.失败.getDesc(), "调用本地服务失败！ManageClient.saveRoleMenu");
            }


        };
    }


}
