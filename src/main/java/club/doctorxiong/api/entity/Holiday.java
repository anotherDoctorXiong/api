package club.doctorxiong.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 节假日表
 * </p>
 *
 * @author xiongxin
 * @since 2021-12-06
 */
@TableName("holiday")
public class Holiday implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 日期
     */
    @TableField("civic_holiday")
    private LocalDate civicHoliday;

    /**
     * 备注
     */
    @TableField("common")
    private String common;

    /**
     * 是否删除，0-正常，1-删除
     */
    @TableField("is_deleted")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 修改时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public LocalDate getCivicHoliday() {
        return civicHoliday;
    }

    public void setCivicHoliday(LocalDate civicHoliday) {
        this.civicHoliday = civicHoliday;
    }
    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }
    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Holiday{" +
            "id=" + id +
            ", civicHoliday=" + civicHoliday +
            ", common=" + common +
            ", isDeleted=" + isDeleted +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
        "}";
    }
}
