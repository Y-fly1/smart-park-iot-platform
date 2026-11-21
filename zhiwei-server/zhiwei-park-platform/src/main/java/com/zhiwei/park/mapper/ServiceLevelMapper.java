package com.zhiwei.park.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiwei.park.domain.ServiceLevel;
import com.zhiwei.park.domain.vo.ServiceLevelVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 服务等级Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Mapper
public interface ServiceLevelMapper  extends BaseMapper<ServiceLevel>
{
    /**
     * 查询服务等级
     * 
     * @param id 服务等级主键
     * @return 服务等级
     */
    public ServiceLevel selectServiceLevelById(Long id);

    /**
     * 查询服务等级列表
     * 
     * @param serviceLevel 服务等级
     * @return 服务等级集合
     */
    public List<ServiceLevelVo> selectServiceLevelList(ServiceLevel serviceLevel);

    /**
     * 新增服务等级
     * 
     * @param serviceLevel 服务等级
     * @return 结果
     */
    public int insertServiceLevel(ServiceLevel serviceLevel);

    /**
     * 修改服务等级
     * 
     * @param serviceLevel 服务等级
     * @return 结果
     */
    public int updateServiceLevel(ServiceLevel serviceLevel);

    /**
     * 删除服务等级
     * 
     * @param id 服务等级主键
     * @return 结果
     */
    public int deleteServiceLevelById(Long id);

    /**
     * 批量删除服务等级
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteServiceLevelByIds(Long[] ids);

}
