package com.zhiwei.park.service;

import java.util.List;
import com.zhiwei.park.domain.EnergyAssessmentReport;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 能效评估报告Service接口
 * 
 * @author ypf
 * @date 2026-10-28
 */
public interface IEnergyAssessmentReportService extends IService<EnergyAssessmentReport>
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
     * 批量删除能效评估报告
     * 
     * @param ids 需要删除的能效评估报告主键集合
     * @return 结果
     */
    public int deleteEnergyAssessmentReportByIds(Long[] ids);

    /**
     * 删除能效评估报告信息
     * 
     * @param id 能效评估报告主键
     * @return 结果
     */
    public int deleteEnergyAssessmentReportById(Long id);
}
