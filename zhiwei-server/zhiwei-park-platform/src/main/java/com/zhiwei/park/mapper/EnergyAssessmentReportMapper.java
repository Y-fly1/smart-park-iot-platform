package com.zhiwei.park.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiwei.park.domain.EnergyAssessmentReport;

/**
 * 能效评估报告Mapper接口
 * 
 * @author ypf
 * @date 2026-10-28
 */
@Mapper
public interface EnergyAssessmentReportMapper extends BaseMapper<EnergyAssessmentReport>
{
    /**
     * 查询能效评估报告
     * 
     * @param id 能效评估报告主键
     * @return 能效评估报告
     */
    public EnergyAssessmentReport selectEnergyAssessmentReportById(Long id);

    /**
     * 查询能效评估报告列表
     * 
     * @param energyAssessmentReport 能效评估报告
     * @return 能效评估报告集合
     */
    public List<EnergyAssessmentReport> selectEnergyAssessmentReportList(EnergyAssessmentReport energyAssessmentReport);

    /**
     * 新增能效评估报告
     * 
     * @param energyAssessmentReport 能效评估报告
     * @return 结果
     */
    public int insertEnergyAssessmentReport(EnergyAssessmentReport energyAssessmentReport);

    /**
     * 修改能效评估报告
     * 
     * @param energyAssessmentReport 能效评估报告
     * @return 结果
     */
    public int updateEnergyAssessmentReport(EnergyAssessmentReport energyAssessmentReport);

    /**
     * 删除能效评估报告
     * 
     * @param id 能效评估报告主键
     * @return 结果
     */
    public int deleteEnergyAssessmentReportById(Long id);

    /**
     * 批量删除能效评估报告
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEnergyAssessmentReportByIds(Long[] ids);
}
