package club.doctorxiong.api.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * 深度票据网登录用户信息
 *
 * @author zhongshenghua
 * @date 2021/5/28
 */
@Data
public class LoginUser implements Serializable {
    private static final long serialVersionUID = -1404543881469558662L;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 公司名称
     */
    private String corpName;

    /**
     * 用户手机号码
     */
    private String mobile;

    /**
     * 登录ip
     */
    private String loginIp;

}
