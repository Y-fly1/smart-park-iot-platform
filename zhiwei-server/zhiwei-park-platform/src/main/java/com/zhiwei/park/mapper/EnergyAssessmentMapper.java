package com.zhiwei.park.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiwei.park.domain.EnergyAssessment;

/**
 * 能效评估记录Mapper接口
 * 
 * @author ypf
 * @date 2026-10-28
 */
@Mapper
public interface EnergyAssessmentMapper extends BaseMapper<EnergyAssessment>
{
    /**
     * 查询能效评估记录
     * 
     * @param id 能效评估记录主键
     * @return 能效评估记录
     */
    public EnergyAssessment selectEnergyAssessmentById(Long id);

    /**
     * 查询能效评估记录列表
     * 
     * @param energyAssessment 能效评估记录
     * @return 能效评估记录集合
     */
    public List<EnergyAssessment> selectEnergyAssessmentList(EnergyAssessment energyAssessment);

    /**
     * 新增能效评估记录
     * 
     * @param energyAssessment 能效评估记录
     * @return 结果
     */
    public int insertEnergyAssessment(EnergyAssessment energyAssessment);

    /**
     * 修改能效评估记录
     * 
     * @param energyAssessment 能效评估记录
     * @return 结果
     */
    public int updateEnergyAssessment(EnergyAssessment energyAssessment);

    /**
     * 删除能效评估记录
     * 
     * @param id 能效评估记录主键
     * @return 结果
     */
    public int deleteEnergyAssessmentById(Long id);

    /**
     * 批量删除能效评估记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEnergyAssessmentByIds(Long[] ids);
}
