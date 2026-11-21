package com.zhiwei.park.domain.vo;

import java.util.List;
import com.zhiwei.park.domain.Contract;
import com.zhiwei.park.domain.dto.TenantFamilyDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 入驻详情VO
 *
 * @author ypf
 * @date 2026-10-30
 */
@Data
@Schema(description = "入驻详情")
public class SettleInDetailVo
{
    /** 企业信息 */
    @Schema(title = "企业信息")
    private TenantVo settleInTenantVo;

    /** 联系人信息列表 */
    @Schema(title = "联系人信息列表")
    private List<TenantFamilyDto> tenantFamilyVoList;

    /** 入驻配置信息 */
    @Schema(title = "入驻配置信息")
    private SettleInConfigVo settleInConfigVo;

    /** 合同信息 */
    @Schema(title = "合同信息")
    private Contract contract;
}
