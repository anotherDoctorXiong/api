package club.doctorxiong.api.service;

import club.doctorxiong.api.common.dto.TokenDTO;
import club.doctorxiong.api.entity.Token;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiongxin
 * @since 2021-12-02
 */
public interface ITokenService extends IService<Token> {

    TokenDTO getTokenCache(String token);

    void invalidateCache(String token);

    /**
     * 获取token
     * @author xiongxin
     * @param token
     * @return club.doctorxiong.miniprogress.entity.Token
     */
    Token getToken (String token);

    /**
     * 手机和类型获取token
     * @author xiongxin
     * @param phone
     * @param orderType
     * @return club.doctorxiong.miniprogress.entity.Token
     */
    Token getTokenByPhoneAndType (String phone,Integer orderType);

    /**
     * 手机和类型获取token
     * @author xiongxin
     * @param phone
     * @return club.doctorxiong.miniprogress.entity.Token
     */
    List<Token> getTokenByPhone (String phone);

}
