package com.zhiwei.park.service.impl;

import java.util.List;
import com.zhiwei.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhiwei.park.mapper.TenantMapper;
import com.zhiwei.park.domain.Tenant;
import com.zhiwei.park.service.ITenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Arrays;

/**
 * 企业表Service业务层处理
 * 
 * @author ypf
 * @date 2026-10-30
 */
@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements ITenantService
{
    @Autowired
    private TenantMapper elderMapper;

    /**
     * 查询企业表
     * 
     * @param id 企业表主键
     * @return 企业表
     */
    @Override
    public Tenant selectTenantById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询企业表列表
     * 
     * @param tenant 企业表
     * @return 企业表
     */
    @Override
    public List<Tenant> selectTenantList(Tenant tenant)
    {
        return elderMapper.selectTenantList(tenant);
    }

    /**
     * 新增企业表
     * 
     * @param tenant 企业表
     * @return 结果
     */
    @Override
    public int insertTenant(Tenant tenant)
    {
        return save(tenant)? 1 : 0;
    }

    /**
     * 修改企业表
     * 
     * @param tenant 企业表
     * @return 结果
     */
    @Override
    public int updateTenant(Tenant tenant)
    {
        return updateById(tenant)? 1 : 0;
    }

    /**
     * 批量删除企业表
     * 
     * @param ids 需要删除的企业表主键
     * @return 结果
     */
    @Override
    public int deleteTenantByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除企业表信息
     * 
     * @param id 企业表主键
     * @return 结果
     */
    @Override
    public int deleteTenantById(Long id)
    {
        return removeById(id)? 1 : 0;
    }
}
