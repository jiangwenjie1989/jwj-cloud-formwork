package com.cloud.user.service.impl;

import com.cloud.common.bean.ChatMessage;
import com.cloud.common.bean.DataContent;
import com.cloud.common.constants.CommConstants;
import com.cloud.common.constants.RedisCacheKeys;
import com.cloud.common.enumeration.*;
import com.cloud.common.redis.RedisNumberCreate;
import com.cloud.common.redis.RedisStringCacheSupport;
import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.SimpleResponse;
import com.cloud.common.syse.HttpCodeE;
import com.cloud.common.syse.SysRespStatusE;
import com.cloud.common.utils.*;
import com.cloud.model.file.FileDTO;
import com.cloud.model.user.AppUser;
import com.cloud.model.user.FriendRequest;
import com.cloud.model.user.UserFriend;
import com.cloud.user.dao.AppUserDao;
import com.cloud.user.dao.FriendRequestDao;
import com.cloud.user.dao.UserFriendDao;
import com.cloud.user.exception.BusinessException;
import com.cloud.user.feign.FileClient;
import com.cloud.user.service.AppUserService;
import com.cloud.user.vo.user.AppUserVO;
import com.cloud.user.vo.user.FriendRequestVO;
import com.cloud.user.vo.user.MyFriendsVO;
import com.cloud.user.websocket.AppUserWebSocket;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @ClassName : AppUserServiceImpl  //类名
 * @Description : app用户service  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-10 16:02  //时间
 */
@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private FileClient fileClient;

    @Autowired
    private AppUserDao appUserDao;

    @Autowired
    private UserFriendDao userFriendDao;

    @Autowired
    private FriendRequestDao friendRequestDao;

    @Autowired
    private RedisStringCacheSupport cacheStr;

    @Autowired
    private RedisNumberCreate redisNumberCreate;


    @Autowired
    private AppUserWebSocket appUserWebSocket;


    @Transactional
    @Override
    public ApiResponse<AppUserVO> registerOrLogin(String phone, String password,String cid)throws Exception {
        ApiResponse<AppUserVO> resp=new ApiResponse<>();
        AppUserVO appUserVO=new AppUserVO();
        Optional<AppUser> optU= appUserDao.findByPhone(phone);
        //存在完成登陆逻辑
        if(optU.isPresent()){
            AppUser appUser = optU.get();
            String passwordDB = appUser.getPassword();
            Long userId = appUser.getId();
            if(passwordDB.equals(Md5Utils.MD5(password+ CommConstants.PASSWORD_SALT))){
                String token = createToken(appUser);

                BeanUtils.copyProperties(appUser,appUserVO);

                appUserVO.setToken(token);
                resp.setData(appUserVO);
                //将token信息缓存
                cacheStr.put(RedisCacheKeys.USER_APP_TOKEN_KEY+userId,token,86400L);
                return resp;
            }else {
                return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "用户名或者密码有误!");
            }
        //注册
        }else {
            AppUser appUser = new AppUser();
            Timestamp nowTimestamp = DateUntil.getNowTimestamp();
            appUser.setPhone(phone);
            appUser.setMacId(cid);
            appUser.setPassword(Md5Utils.MD5(password+ CommConstants.PASSWORD_SALT));
            //调用文件服务
            String username="杰信"+redisNumberCreate.getUserNumber();
            ApiResponse<FileDTO> fileDTOApiResponse = fileClient.creatEqrCode("jiexin_qrcode:" +username);
            if(SysRespStatusE.失败.getDesc().equals(fileDTOApiResponse.getStatus())){
                throw new BusinessException(fileDTOApiResponse.getCode(),fileDTOApiResponse.getMessage());
            }
            appUser.setQrcode(fileDTOApiResponse.getData().getOriginalImage());
            appUser.setStatus(SysUserStatusE.有效.value);
            appUser.setUsername(username);
            appUser.setNickname(username);
            appUser.setCreateTime(nowTimestamp);
            appUser.setUpdateTime(nowTimestamp);
            AppUser appUserDB = appUserDao.save(appUser);
            BeanUtils.copyProperties(appUserDB,appUserVO);
            String token = createToken(appUserDB);
            appUserVO.setToken(token);
            resp.setData(appUserVO);
            //将token信息缓存
            cacheStr.put(RedisCacheKeys.USER_APP_TOKEN_KEY+appUserDB.getId(),token,86400L);
        }
        return resp;
    }

    private String createToken(AppUser appUser) throws Exception {
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("userId", appUser.getId());
        tokenMap.put("username", appUser.getUsername());
        tokenMap.put("nickname", appUser.getNickname());
        tokenMap.put("status", appUser.getStatus());
        tokenMap.put("macId", appUser.getMacId());
        return TokenUtil.createToken(tokenMap);
    }



    @Transactional
    @Override
    public ApiResponse<AppUserVO> updateAppUser(String nickname, String faceImage, String faceImageBig) {
        ApiResponse<AppUserVO> resp=new ApiResponse<>();
        Long userId = ThreadLocalUtil.getUserIdHolder();
        Optional<AppUser> optU= appUserDao.findById(userId);
        if(!optU.isPresent()){
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "用户不存在!");
        }
        AppUserVO appUserVO=new AppUserVO();
        AppUser appUser = optU.get();
        if(!ValidationUtils.StrisNull(nickname)){appUser.setNickname(nickname);}
        if(!ValidationUtils.StrisNull(faceImage)){appUser.setFaceImage(faceImage);}
        if(!ValidationUtils.StrisNull(faceImageBig)){appUser.setFaceImageBig(faceImageBig);}
        appUser.setUpdateTime(DateUntil.getNowTimestamp());
        appUserDao.save(appUser);
        BeanUtils.copyProperties(appUser,appUserVO);
        resp.setData(appUserVO);
        return resp;
    }





    @Override
    public ApiResponse<AppUserVO> searchUser(String friendUsername) {
        ApiResponse<AppUserVO> resp=new ApiResponse<>();
        Optional<AppUser> optU= appUserDao.findByUsername(friendUsername);

        if(!optU.isPresent()){
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "用户不存在!");
        }
        AppUser appUser = optU.get();
        Long friendId = appUser.getId();
        Long userId = ThreadLocalUtil.getUserIdHolder();
        if(friendId.equals(userId)){
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "不能添加自己!");
        }
        Optional<UserFriend> optUf= userFriendDao.findByUserIdAndFriendUserId(userId,friendId);
        if(optUf.isPresent()){
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "该用户已经是你的好友!");
        }

        AppUserVO appUserVO=new AppUserVO();
        BeanUtils.copyProperties(appUser,appUserVO);
        resp.setData(appUserVO);
        return resp;
    }

    @Override
    public SimpleResponse addFriendRequest(String friendUsername) {
        SimpleResponse resp=new SimpleResponse();
        Long userId = ThreadLocalUtil.getUserIdHolder();
        Optional<AppUser> optM= appUserDao.findById(userId);
        if(!optM.isPresent()){
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "用户不存在!");
        }

        Optional<AppUser> optF= appUserDao.findByUsername(friendUsername);
        if(!optF.isPresent()){
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "用户不存在!");
        }
        AppUser appUserF = optF.get();
        Long friendId = appUserF.getId();

        if(friendId.equals(userId)){
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "不能添加自己!");
        }
        Optional<UserFriend> optUf= userFriendDao.findByUserIdAndFriendUserId(userId,friendId);
        if(optUf.isPresent()){
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "该用户已经是你的好友!");
        }
        Optional<FriendRequest> optionalFriendRequest = friendRequestDao.findBySendUserIdAndAcceptUserId(userId, friendId);
        if(!optionalFriendRequest.isPresent()){
            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setSendUserId(userId);
            friendRequest.setAcceptUserId(friendId);
            friendRequest.setCreateTime(DateUntil.getNowTimestamp());
            friendRequestDao.save(friendRequest);
        }
        //异步发送消息给好友
        CompletableFuture.runAsync(() -> {
            DataContent dataContent=new DataContent();
            dataContent.setAction(MsgActionE.请求添加好友.value);
            dataContent.setSendType(MsgSendTypeE.表示点对点发送.value);
            ChatMessage chatMessage=new ChatMessage();
            chatMessage.setSenderId(userId);
            chatMessage.setReceiverId(friendId);
//            AppUserVO appUserVO = new AppUserVO();
//            BeanUtils.copyProperties(optM.get(),appUserVO);
//            chatMessage.setMsg(JSON.toJSONString(appUserVO));
//            chatMessage.setMsgType(MsgTypeE.文本.value);
            dataContent.setChatMsg(chatMessage);
            appUserWebSocket.sendMessage(dataContent);
        }, ThreadPoolServiceUtils.getInstance());


        return resp;
    }

    @Override
    public ApiResponse<FriendRequestVO> queryFriendRequests() {
        ApiResponse<FriendRequestVO> resp=new ApiResponse<>();
        Long userId = ThreadLocalUtil.getUserIdHolder();
        List<FriendRequestVO> friendRequestList=friendRequestDao.queryFriendRequests(userId);
        if(CollectionUtils.isEmpty(friendRequestList)){
            return resp.setReturnErrMsg(resp, HttpCodeE.调用成功.value, SysRespStatusE.成功.getDesc(), "没有数据!");
        }
        resp.setList(friendRequestList);
        return resp;
    }

    @Override
    public ApiResponse<MyFriendsVO> queryMyFriends() {
        ApiResponse<MyFriendsVO> resp=new ApiResponse<>();
        Long userId = ThreadLocalUtil.getUserIdHolder();
        List<MyFriendsVO> friendUserList=userFriendDao.queryMyFriends(userId);
        if(CollectionUtils.isEmpty(friendUserList)){
            return resp.setReturnErrMsg(resp, HttpCodeE.调用成功.value, SysRespStatusE.成功.getDesc(), "没有数据!");
        }
        resp.setList(friendUserList);
        return resp;
    }

    @Transactional
    @Override
    public ApiResponse<MyFriendsVO> agreeOrIgnoreFriends(Long sendUserId, Integer type) {
        ApiResponse<MyFriendsVO> resp=new ApiResponse<>();
        Long userId = ThreadLocalUtil.getUserIdHolder();
        //忽略
        if(type.equals(OperatorFriendRequestTypeE.忽略.value)){
            friendRequestDao.deleteBySendUserIdAndAcceptUserId(sendUserId,userId);
        }else if(type.equals(OperatorFriendRequestTypeE.通过.value)){
            Timestamp nowTimestamp = DateUntil.getNowTimestamp();
            UserFriend userFriendM = createUserFriend(sendUserId, userId, nowTimestamp);
            UserFriend userFriendF = createUserFriend(userId, sendUserId, nowTimestamp);
            ArrayList<UserFriend> list = new ArrayList<>();
            list.add(userFriendM);
            list.add(userFriendF);
            userFriendDao.saveAll(list);
            friendRequestDao.deleteBySendUserIdAndAcceptUserId(sendUserId,userId);
            //  TODO 异步使用websocket主动推送消息到请求发起者，更新他的通讯录列表为最新
            CompletableFuture.runAsync(() -> {
                DataContent dataContent = new DataContent();
                ChatMessage chatMsg = dataContent.getChatMsg();
                chatMsg.setReceiverId(userId);
                dataContent.setChatMsg(chatMsg);
                dataContent.setAction(MsgActionE.拉取好友.value);
                appUserWebSocket.sendMessage(dataContent);
            }, ThreadPoolServiceUtils.getInstance());
        }
        List<MyFriendsVO> friendUserList=userFriendDao.queryMyFriends(userId);
        if(CollectionUtils.isEmpty(friendUserList)){
            return resp.setReturnErrMsg(resp, HttpCodeE.没有数据.value, SysRespStatusE.失败.getDesc(), "没有数据!");
        }
        resp.setList(friendUserList);
        return resp;
    }


    private UserFriend createUserFriend(Long sendUserId, Long userId, Timestamp nowTimestamp) {
        UserFriend userFriendM = new UserFriend();
        userFriendM.setUserId(userId);
        userFriendM.setFriendUserId(sendUserId);
        userFriendM.setCreateTime(nowTimestamp);
        userFriendM.setUpdateTime(nowTimestamp);
        return userFriendM;
    }


}
