package com.zhiwei.park.domain;

import java.math.BigDecimal;
import lombok.Data;
import com.zhiwei.common.annotation.Excel;
import com.zhiwei.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 巡检项对象 inspection_item
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Data
@Schema(description = "巡检项实体")
public class InspectionItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @Schema(title = "编号")
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    @Schema(title = "名称")
    private String name;

    /** 排序号 */
    @Excel(name = "排序号")
    @Schema(title = "排序号")
    private Integer orderNo;

    /** 单位 */
    @Excel(name = "单位")
    @Schema(title = "单位")
    private String unit;

    /** 价格 */
    @Excel(name = "价格")
    @Schema(title = "价格")
    private BigDecimal price;

    /** 图片 */
    @Excel(name = "图片")
    @Schema(title = "图片")
    private String image;

    /** 巡检要求 */
    @Excel(name = "巡检要求")
    @Schema(title = "巡检要求")
    private String inspectionRequirement;

    /** 状态（0：禁用，1：启用） */
    @Excel(name = "状态", readConverterExp = "0=：禁用，1：启用")
    @Schema(title = "状态（0：禁用，1：启用）")
    private Integer status;



}
