package com.zhiwei.park.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiwei.common.utils.DateUtils;
import com.zhiwei.park.domain.ServiceLevel;
import com.zhiwei.park.domain.InspectionItemPlan;
import com.zhiwei.park.domain.dto.InspectionPlanDto;
import com.zhiwei.park.mapper.InspectionItemPlanMapper;
import com.zhiwei.park.domain.vo.InspectionPlanVo;
import com.zhiwei.park.domain.vo.InspectionItemPlanVo;
import com.zhiwei.park.service.IServiceLevelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhiwei.park.mapper.InspectionPlanMapper;
import com.zhiwei.park.domain.InspectionPlan;
import com.zhiwei.park.service.IInspectionPlanService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 巡检计划Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-28
 */
@Transactional
@Service
public class InspectionPlanServiceImpl extends ServiceImpl<InspectionPlanMapper, InspectionPlan> implements IInspectionPlanService {
    @Autowired
    private InspectionPlanMapper inspectionPlanMapper;

    /**
     * 查询巡检计划
     *
     * @param id 巡检计划主键
     * @return 巡检计划
     */
    @Override
    public InspectionPlanVo selectInspectionPlanById(Long id) {
        //查询巡检计划
        InspectionPlan inspectionPlan = inspectionPlanMapper.selectInspectionPlanById(id);
        InspectionPlanVo inspectionPlanVo = new InspectionPlanVo();
        BeanUtils.copyProperties(inspectionPlan, inspectionPlanVo);

        //根据巡检计划的id查询对应的巡检项列表
        List<InspectionItemPlanVo> list = inspectionItemPlanMapper.selectByPlanId(id);
        inspectionPlanVo.setProjectPlans(list);

        return inspectionPlanVo;
    }

    /**
     * 查询巡检计划列表
     *
     * @param inspectionPlan 巡检计划
     * @return 巡检计划
     */
    @Override
    public List<InspectionPlan> selectInspectionPlanList(InspectionPlan inspectionPlan) {
        return inspectionPlanMapper.selectInspectionPlanList(inspectionPlan);
    }

    @Autowired
    private InspectionItemPlanMapper inspectionItemPlanMapper;

    /**
     * 新增巡检计划
     *
     * @param dto 巡检计划
     * @return 结果
     */

    @Override
    public int insertInspectionPlan(InspectionPlanDto dto) {
        // 保存巡检计划
        InspectionPlan inspectionPlan = new InspectionPlan();
        BeanUtils.copyProperties(dto, inspectionPlan);
        inspectionPlan.setCreateTime(DateUtils.getNowDate());
        int count = inspectionPlanMapper.insertInspectionPlan(inspectionPlan);

        //保存巡检项计划中间关系数据
        if (dto.getProjectPlans() != null && !dto.getProjectPlans().isEmpty()) {
            dto.getProjectPlans().forEach(projectPlan -> {
                projectPlan.setPlanId(inspectionPlan.getId());
                projectPlan.setCreateTime(DateUtils.getNowDate());
            });
            //批量保存
            return inspectionItemPlanMapper.batchInsert(dto.getProjectPlans());
        }
        return count;
    }


    @Autowired
    private IServiceLevelService serviceLevelService;


    /**
     * 修改巡检计划
     *
     * @param dto 巡检计划
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateInspectionPlan(InspectionPlanDto dto) {

        long count = serviceLevelService.count(Wrappers.<ServiceLevel>lambdaQuery().eq(ServiceLevel::getLplanId, dto.getId()));
        if (count > 0) {
            throw new RuntimeException("该巡检计划被引用，不能修改");
        }

        // 属性拷贝
        InspectionPlan inspectionPlan = new InspectionPlan();
        BeanUtils.copyProperties(dto, inspectionPlan);

        //判断dto的list（巡检计划与巡检项的关联关系）是否为空，不为空，删除之前的关联关系，再重新批量添加
        if (dto.getProjectPlans() != null && !dto.getProjectPlans().isEmpty()) {
            //删除之前的关联关系  根据巡检计划ID删除
            inspectionItemPlanMapper.deleteByPlanId(inspectionPlan.getId());
            //批量添加
            dto.getProjectPlans().forEach(projectPlan -> {
                projectPlan.setPlanId(inspectionPlan.getId());
                projectPlan.setCreateTime(DateUtils.getNowDate());
            });
            //批量保存
            return inspectionItemPlanMapper.batchInsert(dto.getProjectPlans());
        }

        //不管dto的list空不空，都要修改巡检计划
        return inspectionPlanMapper.updateInspectionPlan(inspectionPlan);

    }

    /**
     * 删除巡检计划信息
     *
     * @param id 巡检计划主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteInspectionPlanById(Long id) {
        long count = serviceLevelService.count(Wrappers.<ServiceLevel>lambdaQuery().eq(ServiceLevel::getLplanId, id));
        if (count > 0) {
            throw new RuntimeException("该巡检计划被引用，不能删除");
        }
        //删除之前的关联关系  根据巡检计划ID删除
        inspectionItemPlanMapper.deleteByPlanId(id);
        return inspectionPlanMapper.deleteInspectionPlanById(id);
    }

    /**
     * 查询所有巡检计划
     *
     * @return
     */
    @Override
    public List<InspectionPlan> listAll() {
        return inspectionPlanMapper.listAll();
    }
}
