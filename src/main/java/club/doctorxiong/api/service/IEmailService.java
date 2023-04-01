package club.doctorxiong.api.service;


import club.doctorxiong.api.entity.Email;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiongxin
 * @since 2021-12-03
 */
public interface IEmailService extends IService<Email> {


    /**
     * 通过邮箱地址和代码获取
     * @author xiongxin
     * @param email 邮箱
     * @return club.doctorxiong.miniprogress.entity.Email
     */
    Email getEmail(String email);


}
