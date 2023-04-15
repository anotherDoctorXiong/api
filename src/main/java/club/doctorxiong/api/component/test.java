package club.doctorxiong.api.component;

import club.doctorxiong.api.common.dto.FundDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;



@Slf4j
public class test implements CommandLineRunner {

    @Autowired
    private FundComponent fundComponent;


    @Override
    public void run(String... args) throws Exception {
        List<FundDTO> list = new ArrayList<>();
        List<String[]> all = fundComponent.allFundCache.get("");
        all.parallelStream().forEach(fund -> {
            log.info(fund[0] + "-" + fund[2]);
            fundComponent.fundCache.get(fund[0]);
        });
    }
}
