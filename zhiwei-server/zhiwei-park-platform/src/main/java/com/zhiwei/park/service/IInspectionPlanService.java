package com.zhiwei.park.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiwei.park.domain.InspectionPlan;
import com.zhiwei.park.domain.dto.InspectionPlanDto;
import com.zhiwei.park.domain.vo.InspectionPlanVo;

/**
 * 巡检计划Service接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
public interface IInspectionPlanService  extends IService<InspectionPlan>
{
    /**
     * 查询巡检计划
     * 
     * @param id 巡检计划主键
     * @return 巡检计划
     */
    public InspectionPlanVo selectInspectionPlanById(Long id);

    /**
     * 查询巡检计划列表
     * 
     * @param inspectionPlan 巡检计划
     * @return 巡检计划集合
     */
    public List<InspectionPlan> selectInspectionPlanList(InspectionPlan inspectionPlan);

    /**
     * 新增巡检计划
     * 
     * @param dto 巡检计划
     * @return 结果
     */
    public int insertInspectionPlan(InspectionPlanDto dto);

    /**
     * 修改巡检计划
     * 
     * @param dto 巡检计划
     * @return 结果
     */
    public int updateInspectionPlan(InspectionPlanDto dto);

    /**
     * 删除巡检计划信息
     * 
     * @param id 巡检计划主键
     * @return 结果
     */
    public int deleteInspectionPlanById(Long id);

    /**
     * 查询所有巡检计划
     * @return
     */
    List<InspectionPlan> listAll();
}
