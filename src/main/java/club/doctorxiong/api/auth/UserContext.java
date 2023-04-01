package club.doctorxiong.api.auth;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户信息上下文，用于保存当前登录用户信息到本地线程
 * @author zhongshenghua
 * @date 2021/5/28
 */
@Slf4j
public class UserContext {

    /**
     * 存储当前线程用户信息
     */
    private static final ThreadLocal<LoginUser> USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 功能：获取用户信息
     *
     */
    public static LoginUser getLoginUser() {
        return USER_THREAD_LOCAL.get();
    }

    /**
     * 设置用户信息
     */
    public static void setLoginUser(LoginUser loginUser){
        USER_THREAD_LOCAL.set(loginUser);
    }

    /**
     * 清除数据
     */
    public static void clearUser(){
        USER_THREAD_LOCAL.remove();
    }

    /**
     * 获取用户id
     */
    public static Integer getUid(){
        return getLoginUser().getUid();
    }



}
