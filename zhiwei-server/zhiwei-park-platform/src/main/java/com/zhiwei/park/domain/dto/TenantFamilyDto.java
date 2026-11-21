package com.zhiwei.park.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 联系人信息DTO
 *
 * @author ypf
 * @date 2026-10-30
 */
@Data
@Schema(description = "联系人信息")
public class TenantFamilyDto
{
    /** 与企业关系 */
    @Schema(title = "与企业关系")
    private String kinship;

    /** 联系人姓名 */
    @Schema(title = "联系人姓名")
    private String name;

    /** 联系人手机号 */
    @Schema(title = "联系人手机号")
    private String phone;
}
