package com.zhiwei.park.service.impl;

import java.io.InputStream;
import java.util.List;
import cn.hutool.json.JSONUtil;
import com.zhiwei.common.exception.ServiceException;
import com.zhiwei.common.exception.base.BaseException;
import com.zhiwei.common.utils.DateUtils;
import com.zhiwei.park.constants.PromptConstant;
import com.zhiwei.park.domain.EnergyAssessmentData;
import com.zhiwei.park.domain.EnergyAssessmentReport;
import com.zhiwei.park.domain.dto.energy.BasicInfo;
import com.zhiwei.park.domain.dto.energy.TenantAssessmentDto;
import com.zhiwei.park.domain.dto.energy.EnergyAssessmentDto;
import com.zhiwei.park.domain.vo.TenantBasicInfoVo;
import com.zhiwei.park.domain.vo.AbnormalDataVO;
import com.zhiwei.park.domain.vo.TenantLevelResultVO;
import com.zhiwei.park.domain.vo.EnergyReportResultVO;
import com.zhiwei.park.service.IEnergyAssessmentReportService;
import com.zhiwei.park.util.PDFUtil;
import com.zhiwei.oss.client.OSSAliyunFileStorageService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhiwei.park.mapper.EnergyAssessmentMapper;
import com.zhiwei.park.domain.EnergyAssessment;
import com.zhiwei.park.service.IEnergyAssessmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;

import static com.zhiwei.common.core.domain.AjaxResult.success;

/**
 * 能效评估记录Service业务层处理
 * 
 * @author ypf
 * @date 2026-10-28
 */
@Service
public class EnergyAssessmentServiceImpl extends ServiceImpl<EnergyAssessmentMapper, EnergyAssessment> implements IEnergyAssessmentService
{
    @Autowired
    private EnergyAssessmentMapper energyAssessmentMapper;

    @Autowired
    private EnergyAssessmentDataServiceImpl collectionService;

    @Autowired
    private ChatClient healthChatClient;

    @Autowired
    private IEnergyAssessmentReportService reportService;

    @Autowired
    private OSSAliyunFileStorageService storageService;



    /**
     * 查询能效评估记录
     *
     * @param id 能效评估记录主键
     * @return 能效评估记录
     */
    @Override
    public EnergyAssessmentData selectEnergyAssessmentById(Long id)
    {
        return collectionService.getById(id);
    }
    /**
     * 查询能效评估记录列表
     * 
     * @param energyAssessment 能效评估记录
     * @return 能效评估记录
     */
    @Override
    public List<EnergyAssessment> selectEnergyAssessmentList(EnergyAssessment energyAssessment)
    {
        return energyAssessmentMapper.selectEnergyAssessmentList(energyAssessment);
    }

    /**
     * 新增或更新能效评估记录
     *
     * @param dto 能效评估收集信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long insertOrUpdateEnergyAssessment(TenantAssessmentDto dto)
    {
        //封装能效评估记录对象
        EnergyAssessment assessment = new EnergyAssessment();
        assessment.setTenantName(dto.getBasicInfo().getTenantName());//企业名称
        assessment.setCreditCode(dto.getBasicInfo().getCreditCode());//统一社会信用代码

        //1. 能效评估记录ID无值,处理逻辑
        if (dto.getId()==null) {
            //1.1 补充新增前对象的额外属性值
            assessment.setSettleInStatus(0);//未入驻
            assessment.setEvaluationProgress(0);//评估中
            assessment.setCreateTime(DateUtils.getNowDate());//创建时间

            //1.2 新增能效评估记录
            save(assessment);
        }
        else {
        //2. 能效评估记录ID有值,处理逻辑
            //2.1 根据ID查询能效评估记录
            EnergyAssessment energyAssessment = getById(dto.getId());

            //2.2 判断能效评估记录是否存在
            if(energyAssessment==null) {
                throw new BaseException("能效评估记录不存在，无法更新");
            }

            //2.3 判断能效评估记录状态是否评估中
            if (energyAssessment.getEvaluationProgress()!=0) {
                throw new BaseException("能效评估记录评估已完成或已取消，无法更新");
            }

            //2.4 更新能效评估记录
            assessment.setId(dto.getId());
            this.updateById(assessment);
        }

        //3.  封装能效评估数据收集对象
        EnergyAssessmentData collection = new EnergyAssessmentData();
        collection.setId(assessment.getId()); //主键ID
        collection.setBasicInfo(JSONUtil.toJsonStr(dto.getBasicInfo())); //基本信息
        collection.setEnergyAssessment(JSONUtil.toJsonStr(dto.getEnergyAssessmentDto()));//能效评估信息
        collection.setElectricity(JSONUtil.toJsonStr(dto.getElectricityUsage()));//日常活动
        collection.setWater(JSONUtil.toJsonStr(dto.getWaterUsage()));//用水
        collection.setGas(JSONUtil.toJsonStr(dto.getGasUsage()));//感知觉及沟通
        collection.setHvac(JSONUtil.toJsonStr(dto.getHvacUsage()));//暖通
        if (dto.getId()==null) {
            collection.setCreateTime(DateUtils.getNowDate());//创建时间
        }

        //4. 新增或更新能效评估收集数据
        collectionService.saveOrUpdate(collection);

        //5. 返回能效评估记录主键ID
        return assessment.getId();
    }


    /**
     * 根据能力等级文字描述，返回对应数字编码
     *
     * @param abilityRating 能力等级文字描述
     * @return 能力等级数字编码
     */
    public String getLevel(String abilityRating){
        switch (abilityRating){
            case "优":
                return "0";
            case "良":
                return "1";
            case "一般":
                return "2";
            case "差":
                return "3";
        }
        throw new BaseException("非法的abilityRating参数！");
    }


    /**
     * AI评估数据
     *
     * @param dto 企业评估表单数据
     * @return 能效评估记录主键ID
     */
    @Override
    public Long assessmentData(TenantAssessmentDto dto) {
    //1.更新保存评估数据,接收前端提交的企业评估表单，把本次评估的基础表单数据保存或者更新到数据库。
        this.insertOrUpdateEnergyAssessment(dto);

    //2.提取各项能力数据从表单里面抽取出四大模块评估结果：

        // 2.1 用电等级、用水等级、燃气等级、暖通等级。
        // 把 “优、良、一般、差” 这类文字等级转换为对应数字编码。
        String dailyLivingLevel = getLevel(dto.getElectricityUsage().getAbilityRating());
        String mentalLevel = getLevel(dto.getWaterUsage().getAbilityRating());
        String perceptionLevel = getLevel(dto.getGasUsage().getAbilityRating());
        String socialLevel = getLevel(dto.getHvacUsage().getAbilityRating());

        // 2.2 同时提取配套风险信息：近 30 天超限告警、设备故障、违规用能尝试、夜间异常、停电停水发生次数
        EnergyAssessmentDto.Recent30Days recent30Days = dto.getEnergyAssessmentDto().getRecent30Days();
        int overLimit = recent30Days.getOverLimit();
        int equipmentFailure = recent30Days.getEquipmentFailure();
        int violation = recent30Days.getViolation();
        int nightAbnormal = recent30Days.getNightAbnormal();
        int outage = recent30Days.getOutage();

        // 2.3 是否患有淘汰设备、私拉乱接，超容用电，画钟测试结果
        EnergyAssessmentDto.DiseaseDiagnosis diseaseDiagnosis = dto.getEnergyAssessmentDto().getDiseaseDiagnosis();
        int obsoleteEquipment = diseaseDiagnosis.getObsoleteEquipment().equals("无") ? 0 : 1;
        int illegalWiring = diseaseDiagnosis.getIllegalWiring().equals("无") ? 0 : 1;
        int overCapacity = diseaseDiagnosis.getOverCapacity().equals("无") ? 0 : 1;
        int leakDetection = dto.getWaterUsage().getLeakDetection().getResult() == 2 ? 1 : 0;

    // 3.第一阶段 AI分析企业多维度能力评估
        // 3.1 使用收集好的企业各项评估信息，拼接组装 AI 提示词 prompt，带上评估规则。
        String healthPrompt = String.format(PromptConstant.ENERGY_LEVEL_PROMPT, dailyLivingLevel, mentalLevel,
                perceptionLevel, socialLevel, overLimit, equipmentFailure, violation,
                nightAbnormal, outage, obsoleteEquipment, illegalWiring, overCapacity, leakDetection);

        // 3.2 通过 ChatClient 调用大模型做能力分析。
        String healthResponse = healthChatClient
                .prompt()
                .user(healthPrompt)
                .call().content();

        //3）处理大模型返回内容，剔除多余文字，只解析出 JSON 格式结果，拿到企业初始能力等级、最终能力等级、等级变化原因。
        TenantLevelResultVO levelResultVO = JSONUtil.toBean(healthResponse, TenantLevelResultVO.class);

        //4）新建一条能效评估报告记录，把评估基础信息、第一阶段 AI 的分析结果先存入数据库。
        EnergyAssessmentReport report = new EnergyAssessmentReport();
        report.setEnergyAssessmentId(String.valueOf(dto.getId()));
        report.setElectricityLevel(dailyLivingLevel);
        report.setWaterLevel(mentalLevel);
        report.setGasLevel(perceptionLevel);
        report.setHvacLevel(socialLevel);
        report.setInitialGrade(levelResultVO.getPreLevel());
        report.setFinalGrade(levelResultVO.getFinalLevel());
        report.setGradeChangeReason(levelResultVO.getReason());
        report.setCreateTime(DateUtils.getNowDate());
        reportService.save(report);

    // 4.第二阶段 AI 分析：能耗账单分析
        // 1）从阿里云 OSS 文件存储下载企业上传的用能检测 PDF 报告文件。
        InputStream inputStream = storageService.download(dto.getEnergyAssessmentDto().getRecent30Days().getEnergyBill());

        // 2）读取解析 PDF，提取出能耗账单全部文本内容。
        String energyReport = PDFUtil.pdfToString(inputStream);

        // 3）将用能检测文本内容组装成第二套 AI 提示词 prompt。
        String energyReportPrompt = String.format(PromptConstant.energyReportPROMPT, energyReport);

        // 4）调用大模型对能耗账单做专业分析。
        String energyReportResponse = healthChatClient
                .prompt()
                .user(energyReportPrompt)
                .call().content();

        // 5）解析 AI 返回结果，拿到能效评分、风险等级、报告总结、身体异常项、各身体系统评分。
        EnergyReportResultVO reportResult = JSONUtil.toBean(
                energyReportResponse, EnergyReportResultVO.class
        );

    // 5.保存评估结果到评估结果表
        // 把两阶段 AI 产出的全部分析结果回填到评估报告记录中。
        // 业务判断规则：
        // 1）是否建议入驻判断规则：能效评分≥60 分，标记为建议入驻；低于 60 不建议入驻。
        Double energyScore = reportResult.getEnergyScore();
        int coreSuggestion = energyScore >= 60 ? 1 : 0;
        report.setCoreSuggestion(coreSuggestion);

        // 2）依据能效分数计算建议服务等级：90 分以上三级巡检，80 以上二级，70 以上一级，60 以上特级，低于 60 不建议入驻。
        report.setRecommendedServiceLevel(
                energyScore >= 90 ? "三级巡检" :
                energyScore >= 80 ? "二级巡检" :
                energyScore >= 70 ? "一级巡检" :
                energyScore >= 60 ? "特级巡检" : "不建议入驻");

        // 3）评估报告表记录入驻状态统一标记为未入驻。
        report.setSettleInStatus(0);

        // 4）能效评估报告表所有字段根据实际情况，该有值的尽量都赋值
        report.setEnergyScore(energyScore.toString());
        report.setRiskLevel(reportResult.getRiskLevel());
        report.setReportSummary(JSONUtil.toJsonStr(reportResult.getSummarize()));
        report.setAbnormalAnalysis(JSONUtil.toJsonStr(reportResult.getAbnormalData()));
        report.setSystemScore(JSONUtil.toJsonStr(reportResult.getSystemScore()));
        reportService.updateById(report);

    // 6.更新评估主记录的状态与入驻建议，修改评估主表数据，写入是否建议入驻的标记，把评估进度设置为【评估已完成】。
        EnergyAssessment assessment = new EnergyAssessment();
        assessment.setId(dto.getId());
        assessment.setSettleInStatus(0);// 入驻状态设置为:未入驻
        assessment.setCoreSuggestion(coreSuggestion);//是否建议入驻
        assessment.setEvaluationProgress(1);//评估进度设置为:已完成
        this.updateEnergyAssessment(assessment);

    // 7.返回评估 ID 整个业务流程结束，返回本次评估记录的唯一ID。
        return dto.getId();

    }


    /**
     * 修改能效评估记录
     * 
     * @param energyAssessment 能效评估记录
     * @return 结果
     */
    @Override
    public int updateEnergyAssessment(EnergyAssessment energyAssessment)
    {
        return updateById(energyAssessment)? 1 : 0;
    }


    /**
     * 取消评估
     *
     * @param id
     * @return
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelAssessment(Long id) {
        EnergyAssessment energyAssessment = getById(id);
        if (energyAssessment == null) {
            throw new ServiceException("评估记录不存在");
        }
        if (energyAssessment.getEvaluationProgress() == 2) {
            throw new ServiceException("该评估已经取消");
        }
        energyAssessment.setEvaluationProgress(2);
        updateById(energyAssessment);
    }



    /**
     * 批量删除能效评估记录
     * 
     * @param ids 需要删除的能效评估记录主键
     * @return 结果
     */
    @Override
    public int deleteEnergyAssessmentByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除能效评估记录信息
     * 
     * @param id 能效评估记录主键
     * @return 结果
     */
    @Override
    public int deleteEnergyAssessmentById(Long id)
    {
        return removeById(id)? 1 : 0;
    }



    /**
     * 查看评估详情(评估结果)
     *
     * @param id
     * @return
     */
    @Override
    public EnergyAssessmentReport getAssessmentDetail(Long id) {

        EnergyAssessmentReport detail = reportService
                .lambdaQuery()
                .eq(EnergyAssessmentReport::getEnergyAssessmentId,id)
                .one();

        return detail;
    }

    /**
     * 查看企业信息详情（入驻字段填充）
     *
     * @param id 评估id
     * @return 企业基本信息
     */
    @Override
    public TenantBasicInfoVo getTenantBasicInfo(Long id) {
        // 1. 查询评估数据采集信息
        EnergyAssessmentData collection = collectionService.getById(id);
        if (collection == null || collection.getBasicInfo() == null) {
            throw new ServiceException("评估记录不存在");
        }

        // 2. 解析基本信息JSON
        BasicInfo basicInfo = JSONUtil.toBean(collection.getBasicInfo(), BasicInfo.class);

        // 3. 组装企业基本信息
        TenantBasicInfoVo vo = new TenantBasicInfoVo();
        vo.setName(basicInfo.getTenantName());
        vo.setCreditCode(basicInfo.getCreditCode());
        vo.setPhone(basicInfo.getTenantContact());
        vo.setNation(basicInfo.getNation());
        vo.setEducationLevel(basicInfo.getEducationLevel());
        vo.setSocialSecurityCard(basicInfo.getSocialSecurityCard());
        vo.setLivingSituation(basicInfo.getLivingSituation());
        vo.setReligiousBelief(basicInfo.getReligiousBelief());
        vo.setEconomicSource(basicInfo.getEconomicSource());
        vo.setMaritalStatus(basicInfo.getMaritalStatus());
        vo.setMedicalPaymentMethod(basicInfo.getMedicalPaymentMethod());

        // 4. 核心建议取自评估记录
        EnergyAssessment assessment = getById(id);
        if (assessment != null) {
            vo.setCoreSuggestion(assessment.getCoreSuggestion());
        }
        return vo;
    }


}
