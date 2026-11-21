package com.zhiwei.park.domain.dto.energy;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 能效评估
 */

@Data
@Schema(description = "能效评估")
public class EnergyAssessmentDto {
    /**
     * 疾病诊断
     */
    @JsonProperty("diseaseDiagnosis")
    @Schema(title = "疾病诊断")
    private DiseaseDiagnosis diseaseDiagnosis;

    /**
     * 最近30天意外事件和能耗账单
     */
    @JsonProperty("recent30Days")
    @Schema(title = "最近30天意外事件和能耗账单")
    private Recent30Days recent30Days;
    
    @Data
    public static class DiseaseDiagnosis {
        /**
         * 淘汰设备
         */
        @JsonProperty("obsoleteEquipment")
        @Schema(title = "淘汰设备")
        private String obsoleteEquipment;
        /**
         * 私拉乱接
         */
        @JsonProperty("illegalWiring")
        @Schema(title = "私拉乱接")
        private String illegalWiring;
        /**
         * 超容用电
         */
        @JsonProperty("overCapacity")
        @Schema(title = "超容用电")
        private String overCapacity;
    }
    
    @Data
    public static class Recent30Days {
        /**
         * 超限告警
         */
        @JsonProperty("overLimit")
        @Schema(title = "超限告警")
        private Integer overLimit;

        /**
         * 夜间异常
         */
        @JsonProperty("nightAbnormal")
        @Schema(title = "夜间异常")
        private Integer nightAbnormal;

        /**
         * 设备故障
         */
        @JsonProperty("equipmentFailure")
        @Schema(title = "设备故障")
        private Integer equipmentFailure;

        /**
         * 违规用能
         */
        @JsonProperty("violation")
        @Schema(title = "违规用能")
        private Integer violation;

        /**
         * 停电停水
         */
        @JsonProperty("outage")
        @Schema(title = "停电停水")
        private Integer outage;

        /**
         * 能耗账单URL
         */
        @JsonProperty("energyBill")
        @Schema(title = "能耗账单URL")
        private String energyBill;
    }
}