package com.zhiwei.park.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiwei.park.domain.Point;
import com.zhiwei.park.mapper.PointMapper;
import com.zhiwei.park.service.IPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 点位Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Service
public class PointServiceImpl extends ServiceImpl<PointMapper, Point> implements IPointService
{
    @Autowired
    private PointMapper bedMapper;

    /**
     * 查询点位
     * 
     * @param id 点位主键
     * @return 点位
     */
    @Override
    public Point selectPointById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询点位列表
     * 
     * @param point 点位
     * @return 点位
     */
    @Override
    public List<Point> selectPointList(Point point)
    {
        return bedMapper.selectPointList(point);
    }

    /**
     * 新增点位
     * 
     * @param point 点位
     * @return 结果
     */
    @Override
    public int insertPoint(Point point)
    {
        return save(point) ? 1 : 0;
    }

    /**
     * 修改点位
     * 
     * @param point 点位
     * @return 结果
     */
    @Override
    public int updatePoint(Point point)
    {
        return updateById(point) ? 1 : 0;
    }

    /**
     * 批量删除点位
     * 
     * @param ids 需要删除的点位主键
     * @return 结果
     */
    @Override
    public int deletePointByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 删除点位信息
     * 
     * @param id 点位主键
     * @return 结果
     */
    @Override
    public int deletePointById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }
}
