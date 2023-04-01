package club.doctorxiong.api.common.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Jin10Response<T> implements Serializable {

    private Integer status;

    private List<T> data;

    private String message;

}
