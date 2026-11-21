package com.zhiwei.park.domain.dto;

import java.util.List;
import com.zhiwei.park.domain.Tenant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 申请入驻DTO
 *
 * @author ypf
 * @date 2026-10-30
 */
@Data
@Schema(description = "申请入驻信息")
public class SettleInApplyDto
{
    /** 评估ID */
    @Schema(title = "评估ID")
    private Long energyAssessmentId;

    /** 企业信息 */
    @Schema(title = "企业信息")
    private Tenant settleInTenantDto;

    /** 联系人信息列表 */
    @Schema(title = "联系人信息列表")
    private List<TenantFamilyDto> tenantFamilyDtoList;

    /** 入驻配置信息 */
    @Schema(title = "入驻配置信息")
    private SettleInConfigDto settleInConfigDto;

    /** 签约合同信息 */
    @Schema(title = "签约合同信息")
    private SettleInContractDto settleInContractDto;
}
