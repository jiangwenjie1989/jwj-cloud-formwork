package com.cloud.gateway.feign.fallback;


import com.cloud.common.response.ApiResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.gateway.feign.ManageClient;
import com.cloud.model.manage.BlackIpDTO;
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
    public ManageClient create(Throwable throwable) {
        return new ManageClient() {
            @Override
            public ApiResponse<BlackIpDTO> selectAllBlackIp() {
                ApiResponse<BlackIpDTO> resp=new ApiResponse<>();
                return resp.setReturnErrMsg(resp, HttpCodeE.调用本地服务失败.value,
                        SysRespStatusE.失败.getDesc(), "调用本地服务失败！ManageClient.selectAllBlackIp");
            }
        };
    }


}
