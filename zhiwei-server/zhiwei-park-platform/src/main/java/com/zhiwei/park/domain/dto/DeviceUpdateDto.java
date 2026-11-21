package com.zhiwei.park.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 设备修改请求DTO
 *
 * @author ypf
 * @date 2026-11-02
 */
@Data
@Schema(description = "设备修改请求DTO")
public class DeviceUpdateDto {

    /** 设备主键 */
    @NotNull(message = "设备主键不能为空")
    @Schema(title = "设备主键")
    private Long id;

    /** 设备名称 */
    @NotBlank(message = "设备名称不能为空")
    @Schema(title = "设备名称")
    private String deviceName;

    /** 绑定位置（随身设备为企业id，固定设备为房间/点位id） */
    @NotBlank(message = "绑定位置不能为空")
    @Schema(title = "绑定位置")
    private String bindingLocation;

    /** 位置类型 0：随身设备 1：固定设备 */
    @NotNull(message = "位置类型不能为空")
    @Schema(title = "位置类型 0：随身设备 1：固定设备")
    private Integer locationType;

    /** 设备标识码（节点id） */
    @NotBlank(message = "设备标识符不能为空")
    @Schema(title = "设备标识码（节点id）")
    private String nodeId;

    /** 物理位置类型 0楼栋 1房间 2点位，随身设备传-1 */
    @NotNull(message = "物理位置类型不能为空")
    @Schema(title = "物理位置类型 0楼栋 1房间 2点位，随身设备传-1")
    private Integer physicalLocationType;

    /** 产品key */
    @NotBlank(message = "产品key不能为空")
    @Schema(title = "产品key")
    private String productKey;

    /** 产品名称 */
    @NotBlank(message = "产品名称不能为空")
    @Schema(title = "产品名称")
    private String productName;

    /** 位置备注 */
    @Schema(title = "位置备注")
    private String deviceDescription;

    /** 备注 */
    @Schema(title = "备注")
    private String remark;
}
