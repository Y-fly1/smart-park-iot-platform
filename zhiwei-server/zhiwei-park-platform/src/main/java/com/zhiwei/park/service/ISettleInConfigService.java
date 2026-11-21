package com.zhiwei.park.service;

import java.util.List;
import com.zhiwei.park.domain.SettleInConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 配置表Service接口
 * 
 * @author ypf
 * @date 2026-10-30
 */
public interface ISettleInConfigService extends IService<SettleInConfig>
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
     * 批量删除配置表
     * 
     * @param ids 需要删除的配置表主键集合
     * @return 结果
     */
    public int deleteSettleInConfigByIds(Long[] ids);

    /**
     * 删除配置表信息
     * 
     * @param id 配置表主键
     * @return 结果
     */
    public int deleteSettleInConfigById(Long id);
}
