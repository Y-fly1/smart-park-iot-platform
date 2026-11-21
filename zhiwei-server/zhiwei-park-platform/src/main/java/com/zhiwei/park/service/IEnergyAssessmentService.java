package com.zhiwei.park.service;

import java.util.List;
import com.zhiwei.park.domain.EnergyAssessment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiwei.park.domain.EnergyAssessmentData;
import com.zhiwei.park.domain.EnergyAssessmentReport;
import com.zhiwei.park.domain.dto.energy.TenantAssessmentDto;
import com.zhiwei.park.domain.vo.TenantBasicInfoVo;

/**
 * 能效评估记录Service接口
 * 
 * @author ypf
 * @date 2026-10-28
 */
public interface IEnergyAssessmentService extends IService<EnergyAssessment>
{
    /**
     * 查询能效评估记录
     *
     * @param id 能效评估记录主键
     * @return 能效评估记录
     */
    public EnergyAssessmentData selectEnergyAssessmentById(Long id);

    /**
     * 查询能效评估记录列表
     * 
     * @param energyAssessment 能效评估记录
     * @return 能效评估记录集合
     */
    public List<EnergyAssessment> selectEnergyAssessmentList(EnergyAssessment energyAssessment);

    /**
     * 新增或更新能效评估记录
     *
     * @param dto 能效评估收集信息
     * @return 结果
     */
    public Long insertOrUpdateEnergyAssessment(TenantAssessmentDto dto);

    /**
     * 修改能效评估记录
     * 
     * @param energyAssessment 能效评估记录
     * @return 结果
     */
    public int updateEnergyAssessment(EnergyAssessment energyAssessment);

    /**
     * AI评估
     * @param dto
     * @return
     */
    Long assessmentData(TenantAssessmentDto dto);

    /**
     * 查看评估详情(评估结果)
     * @param id
     * @return
     */
    EnergyAssessmentReport getAssessmentDetail(Long id);

    /**
     * 批量删除能效评估记录
     * 
     * @param ids 需要删除的能效评估记录主键集合
     * @return 结果
     */
    public int deleteEnergyAssessmentByIds(Long[] ids);

    /**
     * 删除能效评估记录信息
     * 
     * @param id 能效评估记录主键
     * @return 结果
     */
    public int deleteEnergyAssessmentById(Long id);


    /**
     * 取消评估
     * @param id
     * @return
     */
    void cancelAssessment(Long id);

    /**
     * 查看企业信息详情（入驻字段填充）
     *
     * @param id 评估id
     * @return 企业基本信息
     */
    TenantBasicInfoVo getTenantBasicInfo(Long id);

}
