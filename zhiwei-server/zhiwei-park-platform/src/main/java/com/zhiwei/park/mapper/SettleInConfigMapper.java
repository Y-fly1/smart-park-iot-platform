package com.zhiwei.park.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiwei.park.domain.SettleInConfig;

/**
 * 配置表Mapper接口
 * 
 * @author ypf
 * @date 2026-10-30
 */
@Mapper
public interface SettleInConfigMapper extends BaseMapper<SettleInConfig>
{
    /**
     * 查询配置表
     * 
     * @param id 配置表主键
     * @return 配置表
     */
    public SettleInConfig selectSettleInConfigById(Long id);

    /**
     * 查询配置表列表
     * 
     * @param settleInConfig 配置表
     * @return 配置表集合
     */
    public List<SettleInConfig> selectSettleInConfigList(SettleInConfig settleInConfig);

    /**
     * 新增配置表
     * 
     * @param settleInConfig 配置表
     * @return 结果
     */
    public int insertSettleInConfig(SettleInConfig settleInConfig);

    /**
     * 修改配置表
     * 
     * @param settleInConfig 配置表
     * @return 结果
     */
    public int updateSettleInConfig(SettleInConfig settleInConfig);

    /**
     * 删除配置表
     * 
     * @param id 配置表主键
     * @return 结果
     */
    public int deleteSettleInConfigById(Long id);

    /**
     * 批量删除配置表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSettleInConfigByIds(Long[] ids);
}
