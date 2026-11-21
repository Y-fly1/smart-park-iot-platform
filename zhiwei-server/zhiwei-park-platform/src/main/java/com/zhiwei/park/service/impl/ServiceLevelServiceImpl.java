package com.zhiwei.park.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiwei.park.domain.ServiceLevel;
import com.zhiwei.park.domain.vo.ServiceLevelVo;
import com.zhiwei.park.mapper.ServiceLevelMapper;
import com.zhiwei.park.service.IServiceLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务等级Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Service
public class ServiceLevelServiceImpl extends ServiceImpl<ServiceLevelMapper,ServiceLevel> implements IServiceLevelService
{
    @Autowired
    private ServiceLevelMapper serviceLevelMapper;

    /**
     * 查询服务等级
     * 
     * @param id 服务等级主键
     * @return 服务等级
     */
    @Override
    public ServiceLevel selectServiceLevelById(Long id)
    {
        return serviceLevelMapper.selectServiceLevelById(id);
    }

    /**
     * 查询服务等级列表
     * 
     * @param serviceLevel 服务等级
     * @return 服务等级
     */
    @Override
    public List<ServiceLevelVo> selectServiceLevelList(ServiceLevel serviceLevel)
    {
        return serviceLevelMapper.selectServiceLevelList(serviceLevel);
    }

    /**
     * 新增服务等级
     * 
     * @param serviceLevel 服务等级
     * @return 结果
     */
    @Override
    public int insertServiceLevel(ServiceLevel serviceLevel)
    {
        return serviceLevelMapper.insertServiceLevel(serviceLevel);
    }

    /**
     * 修改服务等级
     * 
     * @param serviceLevel 服务等级
     * @return 结果
     */
    @Override
    public int updateServiceLevel(ServiceLevel serviceLevel)
    {
        return serviceLevelMapper.updateServiceLevel(serviceLevel);
    }

    /**
     * 批量删除服务等级
     * 
     * @param ids 需要删除的服务等级主键
     * @return 结果
     */
    @Override
    public int deleteServiceLevelByIds(Long[] ids)
    {
        return serviceLevelMapper.deleteServiceLevelByIds(ids);
    }

    /**
     * 删除服务等级信息
     * 
     * @param id 服务等级主键
     * @return 结果
     */
    @Override
    public int deleteServiceLevelById(Long id)
    {
        return serviceLevelMapper.deleteServiceLevelById(id);
    }

    /**
     * 查询所有启用的服务等级
     *
     * @return 服务等级集合
     */
    @Override
    public List<ServiceLevel> listAll()
    {
        return lambdaQuery().eq(ServiceLevel::getStatus, 1).list();
    }

}
