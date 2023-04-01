package club.doctorxiong.api.service.impl;


import club.doctorxiong.api.entity.Token;
import club.doctorxiong.api.mapper.TokenMapper;
import club.doctorxiong.api.service.ITokenService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiongxin
 * @since 2021-12-02
 */
@Service
public class TokenServiceImpl extends ServiceImpl<TokenMapper, Token> implements ITokenService {

    @Override
    public Token getToken(String token) {
        LambdaQueryWrapper<Token> queryWrapper = new QueryWrapper().lambda();
        queryWrapper.eq(Token::getToken,token);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Token getTokenByPhoneAndType(String phone, Integer orderType) {
        LambdaQueryWrapper<Token> queryWrapper = new QueryWrapper().lambda();
        queryWrapper.eq(Token::getPhone,phone).eq(Token::getType,orderType);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Token> getTokenByPhone(String phone) {
        LambdaQueryWrapper<Token> queryWrapper = new QueryWrapper().lambda();
        queryWrapper.eq(Token::getPhone,phone);
        return baseMapper.selectList(queryWrapper);
    }
}
