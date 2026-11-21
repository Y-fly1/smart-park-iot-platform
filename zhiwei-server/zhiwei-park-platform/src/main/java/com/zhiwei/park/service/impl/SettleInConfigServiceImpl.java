package com.zhiwei.park.service.impl;

import java.util.List;
import com.zhiwei.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhiwei.park.mapper.SettleInConfigMapper;
import com.zhiwei.park.domain.SettleInConfig;
import com.zhiwei.park.service.ISettleInConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Arrays;

/**
 * 配置表Service业务层处理
 * 
 * @author ypf
 * @date 2026-10-30
 */
@Service
public class SettleInConfigServiceImpl extends ServiceImpl<SettleInConfigMapper, SettleInConfig> implements ISettleInConfigService
{
    @Autowired
    private SettleInConfigMapper settleInConfigMapper;

    /**
     * 查询配置表
     * 
     * @param id 配置表主键
     * @return 配置表
     */
    @Override
    public SettleInConfig selectSettleInConfigById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询配置表列表
     * 
     * @param settleInConfig 配置表
     * @return 配置表
     */
    @Override
    public List<SettleInConfig> selectSettleInConfigList(SettleInConfig settleInConfig)
    {
        return settleInConfigMapper.selectSettleInConfigList(settleInConfig);
    }

    /**
     * 新增配置表
     * 
     * @param settleInConfig 配置表
     * @return 结果
     */
    @Override
    public int insertSettleInConfig(SettleInConfig settleInConfig)
    {
        return save(settleInConfig)? 1 : 0;
    }

    /**
     * 修改配置表
     * 
     * @param settleInConfig 配置表
     * @return 结果
     */
    @Override
    public int updateSettleInConfig(SettleInConfig settleInConfig)
    {
        return updateById(settleInConfig)? 1 : 0;
    }

    /**
     * 批量删除配置表
     * 
     * @param ids 需要删除的配置表主键
     * @return 结果
     */
    @Override
    public int deleteSettleInConfigByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除配置表信息
     * 
     * @param id 配置表主键
     * @return 结果
     */
    @Override
    public int deleteSettleInConfigById(Long id)
    {
        return removeById(id)? 1 : 0;
    }
}
