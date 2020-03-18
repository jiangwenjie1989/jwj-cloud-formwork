package com.cloud.user.service;

import com.cloud.common.response.ApiResponse;
import com.cloud.common.response.SimpleResponse;
import com.cloud.user.vo.user.AppUserVO;
import com.cloud.user.vo.user.FriendRequestVO;
import com.cloud.user.vo.user.MyFriendsVO;

public interface AppUserService {

    ApiResponse<AppUserVO> registerOrLogin(String phone, String password,String cid)throws Exception;

    ApiResponse<AppUserVO> searchUser(String friendUsername);

    SimpleResponse addFriendRequest(String friendUsername);

    ApiResponse<FriendRequestVO> queryFriendRequests();

    ApiResponse<MyFriendsVO> queryMyFriends();

    ApiResponse<MyFriendsVO> agreeOrIgnoreFriends(Long sendUserId, Integer type);

    ApiResponse<AppUserVO> updateAppUser(String nickname, String faceImage, String faceImageBig);


}
