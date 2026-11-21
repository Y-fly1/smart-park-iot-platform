package com.zhiwei.park.service.impl;

import java.util.List;
import com.zhiwei.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhiwei.park.mapper.EnergyAssessmentReportMapper;
import com.zhiwei.park.domain.EnergyAssessmentReport;
import com.zhiwei.park.service.IEnergyAssessmentReportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Arrays;

/**
 * 能效评估报告Service业务层处理
 * 
 * @author ypf
 * @date 2026-10-28
 */
@Service
public class EnergyAssessmentReportServiceImpl extends ServiceImpl<EnergyAssessmentReportMapper, EnergyAssessmentReport> implements IEnergyAssessmentReportService
{
    @Autowired
    private EnergyAssessmentReportMapper energyAssessmentReportMapper;

    /**
     * 查询能效评估报告
     * 
     * @param id 能效评估报告主键
     * @return 能效评估报告
     */
    @Override
    public EnergyAssessmentReport selectEnergyAssessmentReportById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询能效评估报告列表
     * 
     * @param energyAssessmentReport 能效评估报告
     * @return 能效评估报告
     */
    @Override
    public List<EnergyAssessmentReport> selectEnergyAssessmentReportList(EnergyAssessmentReport energyAssessmentReport)
    {
        return energyAssessmentReportMapper.selectEnergyAssessmentReportList(energyAssessmentReport);
    }

    /**
     * 新增能效评估报告
     * 
     * @param energyAssessmentReport 能效评估报告
     * @return 结果
     */
    @Override
    public int insertEnergyAssessmentReport(EnergyAssessmentReport energyAssessmentReport)
    {
        return save(energyAssessmentReport)? 1 : 0;
    }

    /**
     * 修改能效评估报告
     * 
     * @param energyAssessmentReport 能效评估报告
     * @return 结果
     */
    @Override
    public int updateEnergyAssessmentReport(EnergyAssessmentReport energyAssessmentReport)
    {
        return updateById(energyAssessmentReport)? 1 : 0;
    }

    /**
     * 批量删除能效评估报告
     * 
     * @param ids 需要删除的能效评估报告主键
     * @return 结果
     */
    @Override
    public int deleteEnergyAssessmentReportByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除能效评估报告信息
     * 
     * @param id 能效评估报告主键
     * @return 结果
     */
    @Override
    public int deleteEnergyAssessmentReportById(Long id)
    {
        return removeById(id)? 1 : 0;
    }
}
