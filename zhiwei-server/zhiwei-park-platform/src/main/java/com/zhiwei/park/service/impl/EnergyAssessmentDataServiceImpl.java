package com.zhiwei.park.service.impl;

import java.util.List;
import com.zhiwei.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhiwei.park.mapper.EnergyAssessmentDataMapper;
import com.zhiwei.park.domain.EnergyAssessmentData;
import com.zhiwei.park.service.IEnergyAssessmentDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Arrays;

/**
 * 能效评估数据采集Service业务层处理
 * 
 * @author ypf
 * @date 2026-10-28
 */
@Service
public class EnergyAssessmentDataServiceImpl extends ServiceImpl<EnergyAssessmentDataMapper, EnergyAssessmentData> implements IEnergyAssessmentDataService
{
    @Autowired
    private EnergyAssessmentDataMapper energyAssessmentDataMapper;

    /**
     * 查询能效评估数据采集
     * 
     * @param id 能效评估数据采集主键
     * @return 能效评估数据采集
     */
    @Override
    public EnergyAssessmentData selectEnergyAssessmentDataById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询能效评估数据采集列表
     * 
     * @param energyAssessmentData 能效评估数据采集
     * @return 能效评估数据采集
     */
    @Override
    public List<EnergyAssessmentData> selectEnergyAssessmentDataList(EnergyAssessmentData energyAssessmentData)
    {
        return energyAssessmentDataMapper.selectEnergyAssessmentDataList(energyAssessmentData);
    }

    /**
     * 新增能效评估数据采集
     * 
     * @param energyAssessmentData 能效评估数据采集
     * @return 结果
     */
    @Override
    public int insertEnergyAssessmentData(EnergyAssessmentData energyAssessmentData)
    {
        return save(energyAssessmentData)? 1 : 0;
    }

    /**
     * 修改能效评估数据采集
     * 
     * @param energyAssessmentData 能效评估数据采集
     * @return 结果
     */
    @Override
    public int updateEnergyAssessmentData(EnergyAssessmentData energyAssessmentData)
    {
        return updateById(energyAssessmentData)? 1 : 0;
    }

    /**
     * 批量删除能效评估数据采集
     * 
     * @param ids 需要删除的能效评估数据采集主键
     * @return 结果
     */
    @Override
    public int deleteEnergyAssessmentDataByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除能效评估数据采集信息
     * 
     * @param id 能效评估数据采集主键
     * @return 结果
     */
    @Override
    public int deleteEnergyAssessmentDataById(Long id)
    {
        return removeById(id)? 1 : 0;
    }
}
