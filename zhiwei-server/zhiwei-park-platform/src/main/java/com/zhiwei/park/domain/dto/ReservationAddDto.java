package com.zhiwei.park.domain.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 新增预约入参
 *
 * @author ypf
 * @date 2026-10-30
 */
@Data
@Schema(description = "新增预约入参")
public class ReservationAddDto
{
    /** 手机号 */
    @NotBlank(message = "手机号不能为空")
    @Schema(title = "手机号")
    private String mobile;

    /** 预约人 */
    @NotBlank(message = "预约人不能为空")
    @Schema(title = "预约人")
    private String name;

    /** 预约时间 */
    @NotNull(message = "预约时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "预约时间")
    private LocalDateTime time;

    /** 预约类型（0：参观预约，1：探访预约） */
    @Schema(title = "预约类型（0：参观预约，1：探访预约）")
    private Integer type;

    /** 家人姓名（企业名称） */
    @NotBlank(message = "家人姓名不能为空")
    @Schema(title = "家人姓名（企业名称）")
    private String visitor;
}
