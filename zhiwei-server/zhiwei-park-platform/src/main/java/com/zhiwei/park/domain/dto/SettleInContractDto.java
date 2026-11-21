package com.zhiwei.park.domain.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 签约合同DTO
 *
 * @author ypf
 * @date 2026-10-30
 */
@Data
@Schema(description = "签约合同信息")
public class SettleInContractDto
{
    /** 协议地址（文件路径或URL） */
    @Schema(title = "协议地址")
    private String agreementPath;

    /** 合同名称 */
    @Schema(title = "合同名称")
    private String contractName;

    /** 签约日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "签约日期")
    private Date signDate;

    /** 丙方姓名 */
    @Schema(title = "丙方姓名")
    private String thirdPartyName;

    /** 丙方手机号 */
    @Schema(title = "丙方手机号")
    private String thirdPartyPhone;
}
