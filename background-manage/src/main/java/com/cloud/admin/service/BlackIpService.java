package com.cloud.admin.service;

import com.cloud.admin.vo.blackip.BlackIpVO;
import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.PageResponse;
import com.cloud.common.response.SimpleResponse;
import com.cloud.model.manage.BlackIpDTO;

public interface BlackIpService {


    PageResponse<BlackIpVO> queryBlackIpPage(Integer currentPage, Integer pageSize);

    SimpleResponse addBlackIp(String ipAddress);

    SimpleResponse deleteBlackIp(Long id);

    ApiResponse<BlackIpDTO> selectAllBlackIp();
}

