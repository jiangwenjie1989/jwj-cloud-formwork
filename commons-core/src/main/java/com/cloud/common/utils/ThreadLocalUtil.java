package com.cloud.common.utils;

/**
 * @ClassName : ThreadLocalUtil  //类名
 * @Description : 共享线程变量工具类  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2020-03-04 14:11  //时间
 */
public class ThreadLocalUtil {

    private static ThreadLocal<Long> userIdHolder = new ThreadLocal();

    private static ThreadLocal<String> usernameHolder = new ThreadLocal();

    public static Long getUserIdHolder() {
        Long userId = userIdHolder.get();
        userIdHolder.remove();
        return userId;
    }

    public static void setUserIdHolder(Long userId) {
        userIdHolder.set(userId);
    }

    public static String getUsernameHolder() {
        String username = usernameHolder.get();
        usernameHolder.remove();
        return username;
    }

    public static void setUsernameHolder(String username) {
        usernameHolder.set(username);
    }
}
