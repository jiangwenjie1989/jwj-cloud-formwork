package com.cloud.admin.service.impl;

import com.cloud.admin.dao.BlackIpDao;
import com.cloud.admin.service.BlackIpService;
import com.cloud.admin.vo.blackip.BlackIpVO;
import com.cloud.common.constants.MessageQueue;
import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.PageResponse;
import com.cloud.common.response.Pagetool;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.common.utils.DateUntil;
import com.cloud.common.utils.ThreadPoolServiceUtils;
import com.cloud.model.manage.BlackIp;
import com.cloud.model.manage.BlackIpDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @ClassName : BlackIpServiceImpl  //类名
 * @Description : 黑名单ip实现  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-08 17:13  //时间
 */
@Slf4j
@Service
public class BlackIpServiceImpl implements BlackIpService {

    @Autowired
    private BlackIpDao blackIpDao;

    @Autowired
    private AmqpTemplate amqpTemplate;


    @Override
    public PageResponse<BlackIpVO> queryBlackIpPage(Integer currentPage, Integer pageSize) {
        PageResponse<BlackIpVO> resp = new PageResponse<>();
        Pagetool<BlackIpVO> pageList = blackIpDao.queryBlackIpPage(currentPage, pageSize);
        if ( CollectionUtils.isEmpty(pageList.getList()) ) {
            return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "没有数据！");
        }
        resp.setPage(pageList);
        return resp;
    }

    @Transactional
    @Override
    public SimpleResponse addBlackIp(String ipAddress) {
        SimpleResponse resp=new SimpleResponse();
        Optional<BlackIp> optBlackIp=blackIpDao.findByIpAddress(ipAddress);
        if (optBlackIp.isPresent()) {
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "该ip存在！");
        }
        BlackIp blackIp=new BlackIp();
        blackIp.setIpAddress(ipAddress);
        blackIp.setCreateTime(DateUntil.getNowTimestamp());
        blackIpDao.save(blackIp);

        CompletableFuture.runAsync(() -> {
            try {
                amqpTemplate.convertAndSend(MessageQueue.BLACK_IP_QUEUE, ipAddress);
                log.info("发送日志到队列表示网关需要重新拉取ip黑名单：{}", ipAddress);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }, ThreadPoolServiceUtils.getInstance());

        return  resp;
    }

    @Transactional
    @Override
    public SimpleResponse deleteBlackIp(Long id) {
        SimpleResponse resp=new SimpleResponse();
        Optional<BlackIp> optBlackIp=blackIpDao.findById(id);
        if (!optBlackIp.isPresent()) {
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "该ip不存在！");
        }
        blackIpDao.deleteById(id);

        CompletableFuture.runAsync(() -> {
            try {
                String ipAddress = optBlackIp.get().getIpAddress();
                amqpTemplate.convertAndSend(MessageQueue.BLACK_IP_QUEUE, ipAddress);
                log.info("发送日志到队列表示网关需要重新拉取ip黑名单：{}", ipAddress);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        },ThreadPoolServiceUtils.getInstance());

        return resp;
    }

    @Override
    public ApiResponse<BlackIpDTO> selectAllBlackIp() {
        ApiResponse<BlackIpDTO> resp=new ApiResponse<>();
        List<BlackIp> blackIpList = blackIpDao.findAll();
        if(!CollectionUtils.isEmpty(blackIpList)){
            List<BlackIpDTO> blackIpDTOList = blackIpList.stream().map(blackIp -> {
                BlackIpDTO blackIpDTO = new BlackIpDTO();
                BeanUtils.copyProperties(blackIp,blackIpDTO);
                return blackIpDTO;
            }).collect(Collectors.toList());
            resp.setList(blackIpDTOList);
            return resp;
        }else {
            return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "没有数据！");
        }
    }
}
