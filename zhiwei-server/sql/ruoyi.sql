

CREATE TABLE `energy_assessment` (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                     `tenant_name` varchar(50) NOT NULL COMMENT '企业名称',
                                     `credit_code` varchar(18) NOT NULL COMMENT '统一社会信用代码码',
                                     `tenant_id` bigint DEFAULT NULL COMMENT '企业id',
                                     `core_suggestion` tinyint(1) DEFAULT NULL COMMENT '核心建议，0-不建议入驻，1-建议入驻',
                                     `settle_in_status` tinyint(1) DEFAULT NULL COMMENT '入驻状态：0-未入驻，1-已入驻',
                                     `evaluation_progress` tinyint(1) DEFAULT NULL COMMENT '评估进度：0-评估中，1-已完成，2-已取消',
                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                     `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                     `create_by` bigint DEFAULT NULL COMMENT '创建人id',
                                     `update_by` bigint DEFAULT NULL COMMENT '更新人id',
                                     `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='能效评估记录表';

CREATE TABLE `energy_assessment_data` (
                                                     `id` bigint NOT NULL COMMENT '主键',
                                                     `basic_info` text COMMENT '基本信息',
                                                     `energy_assessment` text COMMENT '能效评估',
                                                     `electricity` text COMMENT '用电',
                                                     `water` text COMMENT '用水',
                                                     `gas` text COMMENT '感知与沟通',
                                                     `hvac` text COMMENT '暖通',
                                                     `assessment_details` text COMMENT '详细评估报告内容',
                                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                     `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                                     `create_by` bigint DEFAULT NULL COMMENT '创建人id',
                                                     `update_by` bigint DEFAULT NULL COMMENT '更新人id',
                                                     `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='能效评估数据采集';

CREATE TABLE `energy_assessment_report` (
                                            `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                            `energy_assessment_id` bigint unsigned DEFAULT NULL COMMENT '能效评估ID',
                                            `assessment_time` datetime DEFAULT NULL COMMENT '评估时间',
                                            `assessor_name` varchar(50) DEFAULT NULL COMMENT '评估员姓名',
                                            `recommended_area_type` varchar(50) DEFAULT NULL COMMENT '推荐入驻区域类型（如：标准双人间）',
                                            `recommended_service_level` varchar(20) DEFAULT NULL COMMENT '推荐服务等级（如：一级服务等级）',
                                            `energy_focus` text COMMENT '巡检重点（多条用分号分隔，如：血压管控；肢体协助；认知干预）',
                                            `suggestion_description` text COMMENT '建议说明（如：企业基础能效状况稳定，日常生活能部分自理）',
                                            `electricity_level` varchar(10) DEFAULT NULL COMMENT '用电等级',
                                            `water_level` varchar(10) DEFAULT NULL COMMENT '用水等级',
                                            `gas_level` varchar(10) DEFAULT NULL COMMENT '燃气等级',
                                            `hvac_level` varchar(10) DEFAULT NULL COMMENT '暖通等级',
                                            `initial_grade` varchar(50) DEFAULT NULL COMMENT '入驻企业能效初步等级',
                                            `final_grade` varchar(50) DEFAULT NULL COMMENT '入驻企业能力最终等级',
                                            `grade_change_reason` text COMMENT '等级变更依据说明',
                                            `tenant_cooperation` text COMMENT '联系人配合事项（多条用分号分隔）',
                                            `equipment_suggestion` text COMMENT '设备改造建议（如：低盐低脂饮食，控制主食摄入量，避免高糖食物）',
                                            `operation_suggestion` text COMMENT '运行策略建议（如：多关注企业情绪变化，每日进行不少于10分钟的一对一沟通）',
                                            `park_preparation` text COMMENT '园区准备事项（多条用分号分隔，如：提前准备轮椅；房间配置；运维人员适配）',
                                            `energy_score` varchar(20) DEFAULT NULL COMMENT '能效评分',
                                            `risk_level` varchar(20) DEFAULT NULL COMMENT '严重危险(能效, 提示, 风险, 危险, 严重危险)',
                                            `report_summary` text COMMENT '报告总结',
                                            `abnormal_analysis` text COMMENT '异常分析',
                                            `system_score` varchar(255) DEFAULT NULL COMMENT '能效系统分值',
                                            `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
                                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                            `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
                                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                            `remark` text COMMENT '备注',
                                            `core_suggestion` int DEFAULT NULL COMMENT '核心建议，0-不建议入驻，1-建议入驻',
                                            `settle_in_status` tinyint DEFAULT NULL COMMENT '入驻状态：0-未入驻，1-已入驻',
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='能效评估报告表';
