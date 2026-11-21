package com.zhiwei.park.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiwei.park.domain.EnergyAssessmentData;

/**
 * 能效评估数据采集Mapper接口
 * 
 * @author ypf
 * @date 2026-10-28
 */
@Mapper
public interface EnergyAssessmentDataMapper extends BaseMapper<EnergyAssessmentData>
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
     * 删除能效评估数据采集
     * 
     * @param id 能效评估数据采集主键
     * @return 结果
     */
    public int deleteEnergyAssessmentDataById(Long id);

    /**
     * 批量删除能效评估数据采集
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEnergyAssessmentDataByIds(Long[] ids);
}
