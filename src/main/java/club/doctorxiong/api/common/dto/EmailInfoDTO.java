package club.doctorxiong.api.common.dto;


import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author xiongxin
 */
public class EmailInfoDTO implements Serializable {
    private String code;
    private byte fund;
    private String name;
    private LocalDateTime date;
    private int position;
    private String max;
    private String min;
    private String currentPrice;

    public EmailInfoDTO() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public byte getFund() {
        return fund;
    }

    public void setFund(byte fund) {
        this.fund = fund;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }
}
