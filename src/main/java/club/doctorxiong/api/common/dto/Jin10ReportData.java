package club.doctorxiong.api.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Jin10ReportData implements Serializable {

    private String type;

    private String id;

    private LocalDateTime time;

    private ContentData data;
    @Data
    public static class ContentData implements Serializable{
        private String content;

        private String pic;
    }

}
