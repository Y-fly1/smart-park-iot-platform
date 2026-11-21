package com.zhiwei.park.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiwei.park.domain.Point;

import java.util.List;

/**
 * 点位Service接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
public interface IPointService extends IService<Point>
{
    /**
     * 查询点位
     * 
     * @param id 点位主键
     * @return 点位
     */
    public Point selectPointById(Long id);

    /**
     * 查询点位列表
     * 
     * @param point 点位
     * @return 点位集合
     */
    public List<Point> selectPointList(Point point);

    /**
     * 新增点位
     * 
     * @param point 点位
     * @return 结果
     */
    public int insertPoint(Point point);

    /**
     * 修改点位
     * 
     * @param point 点位
     * @return 结果
     */
    public int updatePoint(Point point);

    /**
     * 批量删除点位
     * 
     * @param ids 需要删除的点位主键集合
     * @return 结果
     */
    public int deletePointByIds(Long[] ids);

    /**
     * 删除点位信息
     * 
     * @param id 点位主键
     * @return 结果
     */
    public int deletePointById(Long id);
}
