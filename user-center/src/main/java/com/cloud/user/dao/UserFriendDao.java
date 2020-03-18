package com.cloud.user.dao;


import com.cloud.model.user.AppUser;
import com.cloud.model.user.UserFriend;
import com.cloud.user.base.BaseDao;
import com.cloud.user.repository.AppUserRepository;
import com.cloud.user.repository.UserFriendRepository;
import com.cloud.user.vo.user.FriendRequestVO;
import com.cloud.user.vo.user.MyFriendsVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @ClassName : UserFriendDao  //类名
 * @Description : 用户好友关系  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-10 14:37  //时间
 */
@Repository
public class UserFriendDao extends BaseDao {



    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserFriendRepository userFriendRepository;


    public Optional<UserFriend> findByUserIdAndFriendUserId(Long userId, Long friendId) {
        return userFriendRepository.findByUserIdAndFriendUserId(userId,friendId);
    }

    public List<MyFriendsVO> queryMyFriends(Long userId) {
        //参数
        List<Object> params = Lists.newArrayList();
        params.add(userId);
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT a.id as friendUserId,a.face_image AS friendFaceImage,a.username AS friendUsername,a.nickname AS friendNickname ");
        sql.append(" FROM user_friend f INNER JOIN app_user a ON f.friend_user_id=a.id WHERE f.user_id=?  ");
        sql.append(" ORDER BY f.create_time DESC ");
        return queryForListBean(jdbcTemplate, sql.toString(), MyFriendsVO.class,params.toArray());
    }

    public void saveAll(ArrayList<UserFriend> list) {
        userFriendRepository.saveAll(list);
    }
}
