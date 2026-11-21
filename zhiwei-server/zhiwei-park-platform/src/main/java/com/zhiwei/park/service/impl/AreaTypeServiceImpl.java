package com.zhiwei.park.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiwei.common.exception.base.BaseException;
import com.zhiwei.park.domain.AreaType;
import com.zhiwei.park.mapper.AreaTypeMapper;
import com.zhiwei.park.service.IAreaTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 区域类型Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Service
public class AreaTypeServiceImpl extends ServiceImpl<AreaTypeMapper, AreaType> implements IAreaTypeService
{
    @Autowired
    private AreaTypeMapper areaTypeMapper;

    /**
     * 查询区域类型
     * 
     * @param id 区域类型主键
     * @return 区域类型
     */
    @Override
    public AreaType selectAreaTypeById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询区域类型列表
     * 
     * @param areaType 区域类型
     * @return 区域类型
     */
    @Override
    public List<AreaType> selectAreaTypeList(AreaType areaType)
    {
        return areaTypeMapper.selectAreaTypeList(areaType);
    }

    /**
     * 新增区域类型
     * 
     * @param areaType 区域类型
     * @return 结果
     */
    @Override
    public int insertAreaType(AreaType areaType)
    {
        return save(areaType) ? 1 : 0;
    }

    /**
     * 修改区域类型
     * 
     * @param areaType 区域类型
     * @return 结果
     */
    @Override
    public int updateAreaType(AreaType areaType)
    {
        return updateById(areaType) ? 1 : 0;
    }

    /**
     * 批量删除区域类型
     * 
     * @param ids 需要删除的区域类型主键
     * @return 结果
     */
    @Override
    public int deleteAreaTypeByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 删除区域类型信息
     * 
     * @param id 区域类型主键
     * @return 结果
     */
    @Override
    public int deleteAreaTypeById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }

    /**
     * 按照状态查询房间类型
     * @param status
     * @return
     */
    @Override
    public List<AreaType> findAreaTypeListByStatus(Integer status) {

        if(ObjectUtil.isEmpty(status)){
            throw new BaseException("参数为空");
        }
        LambdaQueryWrapper<AreaType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AreaType::getStatus,status);
        return list(wrapper);
    }
}
