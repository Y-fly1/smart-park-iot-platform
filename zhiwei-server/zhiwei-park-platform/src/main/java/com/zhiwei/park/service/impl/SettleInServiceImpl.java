package com.zhiwei.park.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import cn.hutool.json.JSONUtil;
import com.zhiwei.common.exception.ServiceException;
import com.zhiwei.common.utils.DateUtils;
import com.zhiwei.common.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhiwei.park.mapper.SettleInMapper;
import com.zhiwei.park.domain.Point;
import com.zhiwei.park.domain.SettleIn;
import com.zhiwei.park.domain.SettleInConfig;
import com.zhiwei.park.domain.Contract;
import com.zhiwei.park.domain.Tenant;
import com.zhiwei.park.domain.EnergyAssessment;
import com.zhiwei.park.domain.EnergyAssessmentReport;
import com.zhiwei.park.domain.dto.SettleInApplyDto;
import com.zhiwei.park.domain.dto.SettleInConfigDto;
import com.zhiwei.park.domain.dto.SettleInContractDto;
import com.zhiwei.park.domain.dto.TenantFamilyDto;
import com.zhiwei.park.domain.vo.SettleInConfigVo;
import com.zhiwei.park.domain.vo.SettleInDetailVo;
import com.zhiwei.park.domain.vo.TenantVo;
import com.zhiwei.park.service.IPointService;
import com.zhiwei.park.service.ISettleInConfigService;
import com.zhiwei.park.service.ISettleInService;
import com.zhiwei.park.service.IContractService;
import com.zhiwei.park.service.ITenantService;
import com.zhiwei.park.service.IEnergyAssessmentReportService;
import com.zhiwei.park.service.IEnergyAssessmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 入驻表Service业务层处理
 *
 * @author ypf
 * @date 2026-10-30
 */
@Service
public class SettleInServiceImpl extends ServiceImpl<SettleInMapper, SettleIn> implements ISettleInService
{
    @Autowired
    private SettleInMapper settleInMapper;

    @Autowired
    private IPointService bedService;

    @Autowired
    private ITenantService elderService;

    @Autowired
    private IContractService contractService;

    @Autowired
    private ISettleInConfigService settleInConfigService;

    @Autowired
    private IEnergyAssessmentService energyAssessmentService;

    @Autowired
    private IEnergyAssessmentReportService energyAssessmentReportService;

    /**
     * 查询入驻表
     *
     * @param id 入驻表主键
     * @return 入驻表
     */
    @Override
    public SettleIn selectSettleInById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询入驻表列表
     *
     * @param settleIn 入驻表
     * @return 入驻表
     */
    @Override
    public List<SettleIn> selectSettleInList(SettleIn settleIn)
    {
        return settleInMapper.selectSettleInList(settleIn);
    }

    /**
     * 新增入驻表
     *
     * @param settleIn 入驻表
     * @return 结果
     */
    @Override
    public int insertSettleIn(SettleIn settleIn)
    {
        return save(settleIn)? 1 : 0;
    }

    /**
     * 修改入驻表
     *
     * @param settleIn 入驻表
     * @return 结果
     */
    @Override
    public int updateSettleIn(SettleIn settleIn)
    {
        return updateById(settleIn)? 1 : 0;
    }

    /**
     * 批量删除入驻表
     *
     * @param ids 需要删除的入驻表主键
     * @return 结果
     */
    @Override
    public int deleteSettleInByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除入驻表信息
     *
     * @param id 入驻表主键
     * @return 结果
     */
    @Override
    public int deleteSettleInById(Long id)
    {
        return removeById(id)? 1 : 0;
    }

    /**
     * 申请入驻
     *
     * @param dto 申请入驻信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applySettleIn(SettleInApplyDto dto)
    {
        // 1. 判断评估是否完成，未完成不能入驻
        EnergyAssessment assessment = energyAssessmentService.getById(dto.getEnergyAssessmentId());
        if (assessment == null || assessment.getEvaluationProgress() == null
                || assessment.getEvaluationProgress() != 1) {
            throw new ServiceException("未完成评估不能入驻");
        }

        Tenant elderDto = dto.getSettleInTenantDto();
        if (elderDto == null) {
            throw new ServiceException("企业信息不能为空");
        }

        // 2. 校验企业是否已入驻（基于settle_in表）
        long livingCount = lambdaQuery()
                .eq(SettleIn::getCreditCode, elderDto.getCreditCode())
                .eq(SettleIn::getStatus, 0)
                .count();
        if (livingCount > 0) {
            throw new ServiceException("企业已入驻");
        }

        // 3. 判断点位是否已入驻
        SettleInConfigDto configDto = dto.getSettleInConfigDto();
        if (configDto == null || configDto.getPointId() == null) {
            throw new ServiceException("请选择入驻点位");
        }
        Point point = bedService.getById(configDto.getPointId());
        if (point == null) {
            throw new ServiceException("点位不存在");
        }
        if (point.getPointStatus() != null && point.getPointStatus() == 1) {
            throw new ServiceException("有其他企业已入驻");
        }

        // 4. 更新点位状态为已入驻
        point.setPointStatus(1);
        point.setUpdateTime(DateUtils.getNowDate());
        bedService.updateById(point);

        // 5. 新增或更新企业（企业以前入驻过则更新）
        Tenant tenant = elderService.lambdaQuery()
                .eq(Tenant::getName, elderDto.getName())
                .eq(Tenant::getCreditCode, elderDto.getCreditCode())
                .one();
        if (tenant == null) {
            tenant = elderDto;
            tenant.setPointId(point.getId());
            tenant.setPointCode(point.getPointCode());
            tenant.setStatus(1);
            tenant.setCreateTime(DateUtils.getNowDate());
            elderService.save(tenant);
        } else {
            elderDto.setId(tenant.getId());
            elderDto.setPointId(point.getId());
            elderDto.setPointCode(point.getPointCode());
            elderDto.setStatus(1);
            elderDto.setUpdateTime(DateUtils.getNowDate());
            elderService.updateById(elderDto);
            tenant = elderDto;
        }

        // 6. 新增签约合同
        SettleInContractDto contractDto = dto.getSettleInContractDto();
        if (contractDto != null) {
            Contract contract = new Contract();
            contract.setTenantId(tenant.getId());
            contract.setTenantName(tenant.getName());
            contract.setContractName(contractDto.getContractName());
            contract.setContractNumber(generateContractNumber());
            contract.setAgreementPath(contractDto.getAgreementPath());
            contract.setThirdPartyName(contractDto.getThirdPartyName());
            contract.setThirdPartyPhone(contractDto.getThirdPartyPhone());
            contract.setSignDate(contractDto.getSignDate());
            contract.setStartDate(configDto.getStartDate());
            contract.setEndDate(configDto.getEndDate());
            // 签约日期<=当前时间：已生效(1)，否则未生效(0)
            boolean effective = contractDto.getSignDate() != null
                    && !contractDto.getSignDate().after(new Date());
            contract.setStatus(effective ? 1 : 0);
            contract.setSortOrder(0);
            contract.setCreateTime(DateUtils.getNowDate());
            contractService.save(contract);
        }

        // 7. 新增入驻，联系人信息转JSON保存到remark
        SettleIn settleIn = new SettleIn();
        settleIn.setTenantId(tenant.getId());
        settleIn.setTenantName(tenant.getName());
        settleIn.setCreditCode(tenant.getCreditCode());
        settleIn.setStartDate(configDto.getStartDate());
        settleIn.setEndDate(configDto.getEndDate());
        settleIn.setServiceLevelName(configDto.getServiceLevelName());
        settleIn.setPointCode(point.getPointCode());
        settleIn.setStatus(0);
        settleIn.setSortOrder(0);
        settleIn.setRemark(dto.getTenantFamilyDtoList() == null ? null
                : JSONUtil.toJsonStr(dto.getTenantFamilyDtoList()));
        settleIn.setCreateTime(DateUtils.getNowDate());
        save(settleIn);

        // 8. 新增入驻配置
        SettleInConfig config = new SettleInConfig();
        BeanUtils.copyProperties(configDto, config);
        config.setId(null);
        config.setSettleInId(settleIn.getId());
        if (config.getSortOrder() == null) {
            config.setSortOrder(0);
        }
        config.setCreateTime(DateUtils.getNowDate());
        settleInConfigService.save(config);

        // 9. 修改入驻评估表，并做企业关联
        EnergyAssessment updateAssessment = new EnergyAssessment();
        updateAssessment.setId(assessment.getId());
        updateAssessment.setTenantId(tenant.getId());
        updateAssessment.setSettleInStatus(1);
        energyAssessmentService.updateById(updateAssessment);

        // 10. 修改入驻评估结果表，修改为已入驻
        EnergyAssessmentReport report = energyAssessmentReportService.lambdaQuery()
                .eq(EnergyAssessmentReport::getEnergyAssessmentId, String.valueOf(assessment.getId()))
                .one();
        if (report != null) {
            report.setSettleInStatus(1);
            energyAssessmentReportService.updateById(report);
        }
    }

    /**
     * 生成合同编号：HT + yyyyMMddHHmmss + 四位递增值
     *
     * @return 合同编号
     */
    private String generateContractNumber()
    {
        String date = DateUtils.dateTimeNow("yyyyMMdd");
        long sequence = contractService.lambdaQuery()
                .likeRight(Contract::getContractNumber, "HT" + date)
                .count() + 1;
        return "HT" + DateUtils.dateTimeNow(DateUtils.YYYYMMDDHHMMSS) + String.format("%04d", sequence);
    }

    /**
     * 查询入驻详情
     *
     * @param id 入驻id
     * @return 入驻详情
     */
    @Override
    public SettleInDetailVo getSettleInDetail(Long id)
    {
        SettleIn settleIn = getById(id);
        if (settleIn == null) {
            throw new ServiceException("入驻记录不存在");
        }
        SettleInDetailVo detail = new SettleInDetailVo();

        // 1. 企业信息
        Tenant tenant = elderService.getById(settleIn.getTenantId());
        if (tenant != null) {
            TenantVo elderVo = new TenantVo();
            BeanUtils.copyProperties(tenant, elderVo);
            elderVo.setAge(calculateAge(tenant.getBirthday()));
            detail.setSettleInTenantVo(elderVo);
        }

        // 2. 联系人信息（从入驻表remark中的JSON解析）
        List<TenantFamilyDto> familyList = StringUtils.isEmpty(settleIn.getRemark())
                ? Collections.emptyList()
                : JSONUtil.toList(settleIn.getRemark(), TenantFamilyDto.class);
        detail.setTenantFamilyVoList(familyList);

        // 3. 入驻配置
        SettleInConfig config = settleInConfigService.lambdaQuery()
                .eq(SettleInConfig::getSettleInId, id)
                .one();
        if (config != null) {
            SettleInConfigVo configVo = new SettleInConfigVo();
            BeanUtils.copyProperties(config, configVo);
            configVo.setPointCode(settleIn.getPointCode());
            configVo.setStartDate(settleIn.getStartDate());
            configVo.setEndDate(settleIn.getEndDate());
            detail.setSettleInConfigVo(configVo);
        }

        // 4. 合同信息（取该企业最新的合同）
        Contract contract = contractService.lambdaQuery()
                .eq(Contract::getTenantId, settleIn.getTenantId())
                .orderByDesc(Contract::getId)
                .last("limit 1")
                .one();
        detail.setContract(contract);

        return detail;
    }

    /**
     * 根据出生日期计算年龄
     *
     * @param birthday 出生日期字符串
     * @return 年龄
     */
    private Integer calculateAge(String birthday)
    {
        if (StringUtils.isEmpty(birthday) || birthday.length() < 10) {
            return null;
        }
        try {
            LocalDate birth = LocalDate.parse(birthday.substring(0, 10));
            return Period.between(birth, LocalDate.now()).getYears();
        } catch (Exception e) {
            return null;
        }
    }
}
