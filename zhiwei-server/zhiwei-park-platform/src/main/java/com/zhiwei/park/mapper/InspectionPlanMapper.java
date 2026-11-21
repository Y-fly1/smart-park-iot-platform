package com.zhiwei.park.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiwei.park.domain.InspectionPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 巡检计划Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Mapper
public interface InspectionPlanMapper extends BaseMapper<InspectionPlan>
{
    /**
     * 查询巡检计划
     * 
     * @param id 巡检计划主键
     * @return 巡检计划
     */
    public InspectionPlan selectInspectionPlanById(Long id);

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
     * @param inspectionPlan 巡检计划
     * @return 结果
     */
    public int insertInspectionPlan(InspectionPlan inspectionPlan);

    /**
     * 修改巡检计划
     * 
     * @param inspectionPlan 巡检计划
     * @return 结果
     */
    public int updateInspectionPlan(InspectionPlan inspectionPlan);

    /**
     * 删除巡检计划
     * 
     * @param id 巡检计划主键
     * @return 结果
     */
    public int deleteInspectionPlanById(Long id);

    /**
     * 批量删除巡检计划
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInspectionPlanByIds(Long[] ids);

    @Select("select id,plan_name planName from inspection_plan where status = 1")
    List<InspectionPlan> listAll();
}
