package com.zhiwei.park.service;

import java.util.List;
import com.zhiwei.park.domain.EnergyAssessmentData;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 能效评估数据采集Service接口
 * 
 * @author ypf
 * @date 2026-10-28
 */
public interface IEnergyAssessmentDataService extends IService<EnergyAssessmentData>
{
    /**
     * 查询能效评估数据采集
     * 
     * @param id 能效评估数据采集主键
     * @return 能效评估数据采集
     */
    public EnergyAssessmentData selectEnergyAssessmentDataById(Long id);

    /**
     * 查询能效评估数据采集列表
     * 
     * @param energyAssessmentData 能效评估数据采集
     * @return 能效评估数据采集集合
     */
    public List<EnergyAssessmentData> selectEnergyAssessmentDataList(EnergyAssessmentData energyAssessmentData);

    /**
     * 新增能效评估数据采集
     * 
     * @param energyAssessmentData 能效评估数据采集
     * @return 结果
     */
    public int insertEnergyAssessmentData(EnergyAssessmentData energyAssessmentData);

    /**
     * 修改能效评估数据采集
     * 
     * @param energyAssessmentData 能效评估数据采集
     * @return 结果
     */
    public int updateEnergyAssessmentData(EnergyAssessmentData energyAssessmentData);

    /**
     * 批量删除能效评估数据采集
     * 
     * @param ids 需要删除的能效评估数据采集主键集合
     * @return 结果
     */
    public int deleteEnergyAssessmentDataByIds(Long[] ids);

    /**
     * 删除能效评估数据采集信息
     * 
     * @param id 能效评估数据采集主键
     * @return 结果
     */
    public int deleteEnergyAssessmentDataById(Long id);
}
