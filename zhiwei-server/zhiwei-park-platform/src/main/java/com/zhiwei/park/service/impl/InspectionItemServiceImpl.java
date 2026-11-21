package com.zhiwei.park.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhiwei.common.exception.ServiceException;
import com.zhiwei.common.exception.base.BaseException;
import com.zhiwei.park.domain.InspectionItemPlan;
import com.zhiwei.park.domain.vo.InspectionItemVo;
import com.zhiwei.park.mapper.InspectionItemPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhiwei.park.mapper.InspectionItemMapper;
import com.zhiwei.park.domain.InspectionItem;
import com.zhiwei.park.service.IInspectionItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Arrays;
/**
 * 巡检项Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Service
public class InspectionItemServiceImpl extends ServiceImpl<InspectionItemMapper,InspectionItem> implements IInspectionItemService
{
    @Autowired
    private InspectionItemMapper inspectionItemMapper;

    @Autowired
    private InspectionItemPlanMapper inspectionItemPlanMapper;

    /**
     * 查询巡检项
     * 
     * @param id 巡检项主键
     * @return 巡检项
     */
    @Override
    public InspectionItem selectInspectionItemById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询巡检项列表
     * 
     * @param inspectionItem 巡检项
     * @return 巡检项
     */
    @Override
    public List<InspectionItem> selectInspectionItemList(InspectionItem inspectionItem)
    {
        return inspectionItemMapper.selectInspectionItemList(inspectionItem);
    }

    /**
     * 新增巡检项
     * 
     * @param inspectionItem 巡检项
     * @return 结果
     */
    @Override
    public int insertInspectionItem(InspectionItem inspectionItem)
    {
        return save(inspectionItem)?1:0;
    }

    /**
     * 修改巡检项
     * 
     * @param inspectionItem 巡检项
     * @return 结果
     */
    @Override
    public int updateInspectionItem(InspectionItem inspectionItem)
    {
        //判断巡检项是否被引用，如果被引用，则不能修改
        Long count = inspectionItemPlanMapper.selectCount(Wrappers.<InspectionItemPlan>lambdaQuery().eq(InspectionItemPlan::getProjectId, inspectionItem.getId()));
        if(count > 0){
            throw new ServiceException("巡检项被引用，不能修改");
        }


        return updateById(inspectionItem)?1:0;
    }

    /**
     * 批量删除巡检项
     * 
     * @param ids 需要删除的巡检项主键
     * @return 结果
     */
    @Override
    public int deleteInspectionItemByIds(Long[] ids)
    {
        //判断巡检项是否被引用，如果被引用，则不能修改
        Long count = inspectionItemPlanMapper.selectCount(Wrappers.<InspectionItemPlan>lambdaQuery().in(InspectionItemPlan::getProjectId, Arrays.asList(ids)));
        if(count > 0){
            throw new ServiceException("巡检项被引用，不能删除");
        }
        return removeByIds(Arrays.asList(ids))?1:0;
    }

    /**
     * 删除巡检项信息
     * 
     * @param id 巡检项主键
     * @return 结果
     */
    @Override
    public int deleteInspectionItemById(Long id)
    {
        //判断巡检项是否被引用，如果被引用，则不能修改
        Long count = inspectionItemPlanMapper.selectCount(Wrappers.<InspectionItemPlan>lambdaQuery().eq(InspectionItemPlan::getProjectId, id));
        if(count > 0){
            throw new ServiceException("巡检项被引用，不能删除");
        }

        return removeById(id)?1:0;
    }

    /**
     * 查询所有的巡检项
     * @return
     */
    @Override
    public List<InspectionItemVo> listAll() {
        return inspectionItemMapper.listAll();
    }
}
