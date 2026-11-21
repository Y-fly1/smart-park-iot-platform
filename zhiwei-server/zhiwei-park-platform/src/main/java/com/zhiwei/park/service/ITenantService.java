package com.zhiwei.park.service;

import java.util.List;
import com.zhiwei.park.domain.Tenant;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 企业表Service接口
 * 
 * @author ypf
 * @date 2026-10-30
 */
public interface ITenantService extends IService<Tenant>
{
    /**
     * 查询企业表
     * 
     * @param id 企业表主键
     * @return 企业表
     */
    public Tenant selectTenantById(Long id);

    /**
     * 查询企业表列表
     * 
     * @param tenant 企业表
     * @return 企业表集合
     */
    public List<Tenant> selectTenantList(Tenant tenant);

    /**
     * 新增企业表
     * 
     * @param tenant 企业表
     * @return 结果
     */
    public int insertTenant(Tenant tenant);

    /**
     * 修改企业表
     * 
     * @param tenant 企业表
     * @return 结果
     */
    public int updateTenant(Tenant tenant);

    /**
     * 批量删除企业表
     * 
     * @param ids 需要删除的企业表主键集合
     * @return 结果
     */
    public int deleteTenantByIds(Long[] ids);

    /**
     * 删除企业表信息
     * 
     * @param id 企业表主键
     * @return 结果
     */
    public int deleteTenantById(Long id);
}
