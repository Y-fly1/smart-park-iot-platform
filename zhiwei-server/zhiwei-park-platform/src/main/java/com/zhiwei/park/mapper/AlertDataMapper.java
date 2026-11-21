package com.zhiwei.park.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiwei.park.domain.AlertData;

/**
 * 报警数据Mapper接口
 * 
 * @author ypf
 * @date 2026-11-05
 */
@Mapper
public interface AlertDataMapper extends BaseMapper<AlertData>
{
    /**
     * 查询报警数据
     * 
     * @param id 报警数据主键
     * @return 报警数据
     */
    public AlertData selectAlertDataById(Long id);

    /**
     * 查询报警数据列表
     * 
     * @param alertData 报警数据
     * @return 报警数据集合
     */
    public List<AlertData> selectAlertDataList(AlertData alertData);

    /**
     * 新增报警数据
     * 
     * @param alertData 报警数据
     * @return 结果
     */
    public int insertAlertData(AlertData alertData);

    /**
     * 修改报警数据
     * 
     * @param alertData 报警数据
     * @return 结果
     */
    public int updateAlertData(AlertData alertData);

    /**
     * 删除报警数据
     * 
     * @param id 报警数据主键
     * @return 结果
     */
    public int deleteAlertDataById(Long id);

    /**
     * 批量删除报警数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAlertDataByIds(Long[] ids);

    /**
     * 根据角色名称查询用户ID
     *
     * @param roleName 角色名称
     * @return 用户ID集合
     */
    public List<Long> selectUserIdsByRoleName(@Param("roleName") String roleName);

    /**
     * 查询移动设备绑定企业的巡检员用户ID
     *
     * @param iotId 物联网设备ID
     * @return 巡检员用户ID集合
     */
    public List<Long> selectInspectionUserIdsByMobileDevice(@Param("iotId") String iotId);

    /**
     * 查询固定设备绑定企业的巡检员用户ID
     *
     * @param iotId 物联网设备ID
     * @return 巡检员用户ID集合
     */
    public List<Long> selectInspectionUserIdsByFixedDevice(@Param("iotId") String iotId);
}
