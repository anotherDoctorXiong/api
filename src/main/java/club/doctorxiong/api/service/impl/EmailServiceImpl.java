package club.doctorxiong.api.service.impl;

import club.doctorxiong.api.entity.Email;
import club.doctorxiong.api.mapper.EmailMapper;
import club.doctorxiong.api.service.IEmailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiongxin
 * @since 2021-12-03
 */
@Service
public class EmailServiceImpl extends ServiceImpl<EmailMapper, Email> implements IEmailService {



    @Override
    public Email getEmail(String email) {
        LambdaQueryWrapper<Email> queryWrapper = new QueryWrapper().lambda();
        queryWrapper.eq(Email::getEmail,email);
        return baseMapper.selectOne(queryWrapper);
    }

}
