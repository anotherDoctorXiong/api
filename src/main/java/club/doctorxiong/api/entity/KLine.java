package club.doctorxiong.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiongxin
 * @since 2021-12-04
 */
@TableName("k_line")
public class KLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "code")
    private String code;

    private LocalDateTime validTime;

    private LocalDate startDate;

    private byte[] data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public LocalDateTime getValidTime() {
        return validTime;
    }

    public void setValidTime(LocalDateTime validTime) {
        this.validTime = validTime;
    }
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String[][]  getArr(){
        String[][] obj= null;
        if(this.data==null){
            return null;
        }
        try{
            //建立对象序列化输入流
            ObjectInputStream in = new ObjectInputStream((new ByteArrayInputStream(this.data)));
            //按制定类型还原对象
            obj = (String[][])in.readObject();
        }catch(Exception e){
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public String toString() {
        return "KLine{" +
            "code=" + code +
            ", validTime=" + validTime +
            ", startDate=" + startDate +
            ", data=" + data +
        "}";
    }
}
