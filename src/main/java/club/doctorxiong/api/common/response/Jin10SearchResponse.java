package club.doctorxiong.api.common.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class Jin10SearchResponse<T> implements Serializable {

    private Integer status;

    private T data;

    private String message;

}
