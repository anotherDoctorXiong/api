package club.doctorxiong.api.common.dto;

import club.doctorxiong.api.common.LocalDateTimeFormatter;
import lombok.Data;

import java.io.Serializable;



/**
 * @author XIONGXIN
 * @title: NetWorthData
 * @date 2021/4/2 0:28
 */
@Data
public class NetWorthDataDTO implements Serializable {
     private Long x;
     private String y;
     private String equityReturn;
     private String unitMoney;

     public String[] getDataArr(){
          String[] arrays = {LocalDateTimeFormatter.getLocalDateByTimestamp(x).toString(),y,equityReturn,unitMoney};
          return arrays;
     }
}
