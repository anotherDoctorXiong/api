package club.doctorxiong.api.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Jin10SearchData implements Serializable {

    private List<Jin10ReportData> list;

}
