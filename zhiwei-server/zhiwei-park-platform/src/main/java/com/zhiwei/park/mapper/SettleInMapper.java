package com.zhiwei.park.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiwei.park.domain.SettleIn;

/**
 * 入驻表Mapper接口
 * 
 * @author ypf
 * @date 2026-10-30
 */
@Mapper
public interface SettleInMapper extends BaseMapper<SettleIn>
{
    /**
     * 查询入驻表
     * 
     * @param id 入驻表主键
     * @return 入驻表
     */
    public SettleIn selectSettleInById(Long id);

    /**
     * 查询入驻表列表
     * 
     * @param settleIn 入驻表
     * @return 入驻表集合
     */
    public List<SettleIn> selectSettleInList(SettleIn settleIn);

    /**
     * 新增入驻表
     * 
     * @param settleIn 入驻表
     * @return 结果
     */
    public int insertSettleIn(SettleIn settleIn);

    /**
     * 修改入驻表
     * 
     * @param settleIn 入驻表
     * @return 结果
     */
    public int updateSettleIn(SettleIn settleIn);

    /**
     * 删除入驻表
     * 
     * @param id 入驻表主键
     * @return 结果
     */
    public int deleteSettleInById(Long id);

    /**
     * 批量删除入驻表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSettleInByIds(Long[] ids);
}
