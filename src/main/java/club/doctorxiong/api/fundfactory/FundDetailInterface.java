package club.doctorxiong.api.fundfactory;


import club.doctorxiong.api.common.dto.FundDTO;

/**
 * @author XIONGXIN
 * @title: FundDetailInterface
 * @projectName miniprogress
 * @date 2021/1/30 13:54
 */
public interface FundDetailInterface {

    /**
     * 填充基金信息
     * @author xiongxin
     * @return void
     */
    void fillFundDetail(String input, FundDTO fundDTODetail);
}
