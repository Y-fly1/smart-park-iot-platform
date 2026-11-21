package com.zhiwei.park.domain.vo;

import com.zhiwei.park.domain.Tenant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 企业VO（详情展示，含年龄）
 *
 * @author ypf
 * @date 2026-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "企业详情")
public class TenantVo extends Tenant
{
    private static final long serialVersionUID = 1L;

    /** 年龄 */
    @Schema(title = "年龄")
    private Integer age;
}
