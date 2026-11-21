package com.zhiwei.park.domain;

import com.zhiwei.common.annotation.Excel;
import com.zhiwei.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 楼栋对象 building
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Data
@Schema(description = "楼栋实体")
public class Building extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Schema(title = "id")
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    @Schema(title = "名称")
    private String name;

    /** 编号 */
    @Excel(name = "编号")
    @Schema(title = "编号")
    private Long code;

}
