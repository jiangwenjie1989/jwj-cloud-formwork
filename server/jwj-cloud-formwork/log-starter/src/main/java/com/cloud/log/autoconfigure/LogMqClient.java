package com.cloud.log.autoconfigure;


import com.cloud.common.constants.MessageQueue;
import com.cloud.common.utils.DateUntil;
import com.cloud.common.utils.ThreadLocalUtil;

import com.cloud.model.log.SysLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import java.util.concurrent.CompletableFuture;

/**
 * 通过mq发送日志<br>
 * 在LogAutoConfiguration中将该类声明成Bean，用时直接@Autowired即可
 * @author jiangwenjie
 */
public class LogMqClient {

    private static final Logger logger = LoggerFactory.getLogger(LogMqClient.class);

    private AmqpTemplate amqpTemplate;

    public LogMqClient(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendLogMsg(String module, String username, String params, String remark, Integer flag) {
        CompletableFuture.runAsync(() -> {
            try {
                SysLog log = new SysLog();
                log.setCreateTime(DateUntil.getNowTimestamp());
                if (StringUtils.isNotBlank(username)) {
                    log.setUsername(username);
                } else {
                    log.setUsername(ThreadLocalUtil.getUsernameHolder());
                }
                log.setFlag(flag);
                log.setDoName(module);
                log.setParams(params);
                log.setRemark(remark);
                amqpTemplate.convertAndSend(MessageQueue.LOG_QUEUE, log);
                logger.info("发送日志到队列：{}", log);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        });
    }
}
