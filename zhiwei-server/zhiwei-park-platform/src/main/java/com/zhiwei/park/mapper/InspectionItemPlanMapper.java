package com.zhiwei.park.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiwei.park.domain.InspectionItemPlan;
import com.zhiwei.park.domain.dto.InspectionItemPlanDto;
import com.zhiwei.park.domain.vo.InspectionItemPlanVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 巡检计划和项目关联Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Mapper
public interface InspectionItemPlanMapper  extends BaseMapper<InspectionItemPlan>
{
    /**
     * 查询巡检计划和项目关联
     * 
     * @param id 巡检计划和项目关联主键
     * @return 巡检计划和项目关联
     */
    public InspectionItemPlan selectInspectionItemPlanById(Long id);

    /**
     * 查询巡检计划和项目关联列表
     * 
     * @param inspectionItemPlan 巡检计划和项目关联
     * @return 巡检计划和项目关联集合
     */
    public List<InspectionItemPlan> selectInspectionItemPlanList(InspectionItemPlan inspectionItemPlan);

    /**
     * 新增巡检计划和项目关联
     * 
     * @param inspectionItemPlan 巡检计划和项目关联
     * @return 结果
     */
    public int insertInspectionItemPlan(InspectionItemPlan inspectionItemPlan);

    /**
     * 修改巡检计划和项目关联
     * 
     * @param inspectionItemPlan 巡检计划和项目关联
     * @return 结果
     */
    public int updateInspectionItemPlan(InspectionItemPlan inspectionItemPlan);

    /**
     * 删除巡检计划和项目关联
     * 
     * @param id 巡检计划和项目关联主键
     * @return 结果
     */
    public int deleteInspectionItemPlanById(Long id);

    /**
     * 批量删除巡检计划和项目关联
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInspectionItemPlanByIds(Long[] ids);


    int batchInsert(List<InspectionItemPlanDto> projectPlans);

    List<InspectionItemPlanVo> selectByPlanId(Long planId);

    @Delete("delete from inspection_item_plan where plan_id = #{planId}")
    void deleteByPlanId(Long planId);
}
