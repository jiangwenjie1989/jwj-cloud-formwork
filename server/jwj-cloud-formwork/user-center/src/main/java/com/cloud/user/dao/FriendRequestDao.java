package com.cloud.user.dao;


import com.cloud.model.user.FriendRequest;
import com.cloud.user.base.BaseDao;
import com.cloud.user.repository.FriendRequestRepository;
import com.cloud.user.vo.user.FriendRequestVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


/**
 * @ClassName : UserFriendDao  //类名
 * @Description : 用户好友关系  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-10 14:37  //时间
 */
@Repository
public class FriendRequestDao extends BaseDao {



    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FriendRequestRepository friendRequestRepository;


    public Optional<FriendRequest> findBySendUserIdAndAcceptUserId(Long userId, Long friendId) {
        return  friendRequestRepository.findBySendUserIdAndAcceptUserId(userId,friendId);
    }

    public void save(FriendRequest friendRequest) {
        friendRequestRepository.save(friendRequest);
    }

    public List<FriendRequestVO> queryFriendRequests(Long userId) {
        //参数
		List<Object> params = Lists.newArrayList();
        params.add(userId);
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT a.id AS sendUserId,a.username AS sendUsername ,a.face_image AS sendFaceImage,a.nickname AS sendNickname ");
        sql.append(" FROM  app_user a INNER JOIN friend_request f ON a.id=f.send_user_id ");
        sql.append(" WHERE f.accept_user_id= ? ");
        sql.append(" ORDER BY f.create_time DESC ");
        return queryForListBean(jdbcTemplate, sql.toString(), FriendRequestVO.class,params.toArray());
    }

    public void deleteBySendUserIdAndAcceptUserId(Long sendUserId, Long userId) {
        friendRequestRepository.deleteBySendUserIdAndAcceptUserId(sendUserId,userId);
    }
}
