package com.cloud.user.websocket;

import com.alibaba.fastjson.JSON;
import com.cloud.common.bean.ChatMessage;
import com.cloud.common.bean.DataContent;
import com.cloud.common.bean.RedisMassgae;
import com.cloud.common.constants.RedisCacheKeys;
import com.cloud.common.redis.RedisStringCacheSupport;
import com.cloud.common.utils.TokenUtil;
import com.cloud.common.utils.ValidationUtils;
import com.cloud.model.message.ChatMsgDTO;
import com.cloud.user.feign.MessageClient;
import com.cloud.user.handler.MsgActionHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yeauty.annotation.*;
import org.yeauty.pojo.ParameterMap;
import org.yeauty.pojo.Session;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName : AppUserWebSocket  //类名
 * @Description : websocket服务类  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-10 22:42  //时间
 */
@Slf4j
@ServerEndpoint(prefix = "netty-websocket")
@Component
public class AppUserWebSocket {

	@Value("${channel.im.name}")
	private String imChannel;

	@Autowired
	private MessageClient messageClient;

	@Autowired
	private MsgActionHandler msgActionHandler;



	@Autowired
	private RedisStringCacheSupport redisString;
	


	public static final Map<String, Session> userSocketSessionMap;

	static {
		userSocketSessionMap = new ConcurrentHashMap<String, Session>();
	}

	/**
	 * 建立连接时候会执行
	 * @param session
	 * @param headers
	 * @param parameterMap
	 * @throws IOException
	 */
	@OnOpen
	public void onOpen(Session session, HttpHeaders headers, ParameterMap parameterMap) throws IOException {
		String token = parameterMap.getParameter("token");
		if(!ValidationUtils.StrisNull(token)&&TokenUtil.verifyToken(token)){
			String userId = TokenUtil.getField(token, "userId");
			String tokenDB = redisString.getCached(RedisCacheKeys.USER_APP_TOKEN_KEY + userId);
			if(tokenDB!=null){
				session.setAttribute("userId", userId);
				//保存socket回话 session
				userSocketSessionMap.put(userId,session);
			}else {
				log.info("登录过期用户id:{}",userId);
			}
		}else {
			log.info("token为空");
		}
	}

	/**
	 * 收到消息后执行
	 * @param session
	 * @param message
	 */
	@OnMessage
	public void onMessage(Session session, String message) {
		DataContent dataContent = JSON.parseObject(message, DataContent.class);
		Integer action = dataContent.getAction();
		//优化代码 消息动作处理
		msgActionHandler.process(action,dataContent);
/*		if(action.equals(MsgActionE.聊天消息.value)){
			ChatMessage chatMsg = dataContent.getChatMsg();
			String msgText = chatMsg.getMsg();
			System.out.println("收到消息"+msgText);
			Long receiverId = chatMsg.getReceiverId();

			//调用消息服务保存消息到数据库
			ChatMsgDTO chatMsgDTO = createChatMsgDTO(dataContent, chatMsg, msgText, receiverId);
			SimpleResponse simpleResponse = messageClient.saveChatMsg(chatMsgDTO);
			if(SysRespStatusE.成功.getDesc().equals(simpleResponse.getStatus())){
				String msgId = simpleResponse.getExtData().get("msgId").toString();
				chatMsg.setMsgId(msgId);
				//发送消息到redis
				sendMessage(dataContent);
			}

		}else if(action.equals(MsgActionE.消息签收.value)) {
			// 扩展字段在action=3类型的消息中，代表需要去签收的消息id，逗号间隔
			String msgIdsStr = dataContent.getExtand();
			if(!ValidationUtils.StrisNull(msgIdsStr)){
				//异步调用消息服务批量签收消息
				CompletableFuture.runAsync(() -> {
					messageClient.updateChatMsg(msgIdsStr);
				}, ThreadPoolServiceUtils.getInstance());
			}
		}else if(action.equals(MsgActionE.客户端保持心跳.value)) {
//			String userId = session.getAttribute("userId").toString();
//			System.out.println("收到来自用户id为[" + userId + "]的心跳包...");
		}*/
	}



	/**
	 * 连接关闭的时候会执行
	 * @param session
	 * @throws IOException
	 */
	@OnClose
	public void onClose(Session session) throws IOException {
		String userId = session.getAttribute("userId").toString();
		System.out.println("断开连接用户id="+userId);
		if(!ValidationUtils.StrisNull(userId)){
			userSocketSessionMap.remove(userId);
		}
	}

	/**
	 * 发生错误的时候执行
	 * @param session
	 * @param throwable
	 */
	@OnError
	public void onError(Session session, Throwable throwable) {
		String userId = session.getAttribute("userId").toString();
		if(!ValidationUtils.StrisNull(userId)){
			userSocketSessionMap.remove(userId);
			session.close();
		}
		throwable.printStackTrace();
	}



	/**
	 * 收到二进制数据执行
	 * @param session
	 * @param bytes
	 */
	@OnBinary
	public void onBinary(Session session, byte[] bytes) {
		for (byte b : bytes) {
			System.out.println(b);
		}
		session.sendBinary(bytes);
	}

	/**
	 * 空闲的时候执行
	 * @param session
	 * @param evt
	 */
	@OnEvent
	public void onEvent(Session session, Object evt) {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
			switch (idleStateEvent.state()) {
			case READER_IDLE:
				System.out.println("read idle");
				break;
			case WRITER_IDLE:
				System.out.println("write idle");
				break;
			case ALL_IDLE:
				System.out.println("all idle");
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 给指定用户发送消息
	 * 
	 * @param message
	 * @param userId
	 * @throws IOException
	 */
	public void sendMessageToUser(final String message, Long userId) {
		Session session = userSocketSessionMap.get(String.valueOf(userId));
		System.out.println("发送消息"+JSON.toJSONString(message));
		if (session != null && session.isOpen()) {
			session.sendText(message);
		}else {
			// TODO session为空代表用户离线，推送消息（JPush，个推，小米推送）


		}
	}

	/**
	 * 给所有在线用户发送消息
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void broadcast(final String message) {
		Iterator<Map.Entry<String, Session>> it = userSocketSessionMap.entrySet().iterator();
		while (it.hasNext()) {
			final Map.Entry<String, Session> entry = it.next();
			entry.getValue().sendText(message);
		}
	}


	/**
	 * 发送消息到redis
	 * 
	 * @param dataContent
	 */
	public void sendMessage(DataContent dataContent) {
		redisString.sendMessage(imChannel, JSON.toJSONString(new RedisMassgae(imChannel,dataContent)));
	}

//	private ChatMsgDTO createChatMsgDTO(DataContent dataContent, ChatMessage chatMsg, String msgText, Long receiverId) {
//		ChatMsgDTO chatMsgDTO = new ChatMsgDTO();
//		chatMsgDTO.setSendUserId(chatMsg.getSenderId());
//		chatMsgDTO.setAcceptUserId(receiverId);
//		chatMsgDTO.setContent(msgText);
//		chatMsgDTO.setSendType(dataContent.getSendType());
//		chatMsgDTO.setMsgType(chatMsg.getMsgType());
//		return chatMsgDTO;
//	}

}
