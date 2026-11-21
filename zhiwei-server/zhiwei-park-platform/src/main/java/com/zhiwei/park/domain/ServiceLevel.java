package com.zhiwei.park.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.zhiwei.common.annotation.Excel;
import com.zhiwei.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 服务等级对象 service_level
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Schema(description = "服务等级实体")
public class ServiceLevel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Schema(title = "主键ID")
    private Long id;

    /** 等级名称 */
    @Excel(name = "等级名称")
    @Schema(title = "等级名称")
    private String name;

    /** 巡检计划ID */
    @Excel(name = "巡检计划ID")
    @Schema(title = "巡检计划ID")
    private Long lplanId;

    /** 服务费用 */
    @Excel(name = "服务费用")
    @Schema(title = "服务费用")
    private BigDecimal fee;

    /** 状态（0：禁用，1：启用） */
    @Excel(name = "状态", readConverterExp = "0=：禁用，1：启用")
    @Schema(title = "状态（0：禁用，1：启用）")
    private Integer status;

    /** 等级说明 */
    @Excel(name = "等级说明")
    @Schema(title = "等级说明")
    private String description;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setLplanId(Long lplanId) 
    {
        this.lplanId = lplanId;
    }

    public Long getLplanId() 
    {
        return lplanId;
    }
    public void setFee(BigDecimal fee) 
    {
        this.fee = fee;
    }

    public BigDecimal getFee() 
    {
        return fee;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("lplanId", getLplanId())
            .append("fee", getFee())
            .append("status", getStatus())
            .append("description", getDescription())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
