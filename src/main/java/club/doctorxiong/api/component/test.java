package club.doctorxiong.api.component;

import club.doctorxiong.api.common.dto.CompressFundDTO;
import club.doctorxiong.api.common.dto.FundDTO;
import club.doctorxiong.api.common.dto.KLineDTO;
import club.doctorxiong.api.common.request.KLineRequest;
import club.doctorxiong.api.service.impl.TokenServiceImpl;
import club.doctorxiong.api.uitls.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class test implements CommandLineRunner {

    @Autowired
    private FundComponent fundComponent;

    @Autowired
    private StockComponent stockComponent;

    @Autowired
    private TokenServiceImpl tokenService;


    @Override
    public void run(String... args) throws Exception {
        /*List<FundDTO> list = new ArrayList<>();
        List<String[]> all = fundComponent.allFundCache.get("");
        all.parallelStream().forEach(fund -> {
            log.info("info1" + fund[0] + "-" + fund[2]);
            FundDTO fundDTO = fundComponent.getFundDTO(fund[0]);

            System.out.println("info2" +fundDTO.getCode() + fundDTO.getName());
        });*/

    }
}
