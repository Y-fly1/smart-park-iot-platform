package com.zhiwei.park.service;

import java.util.List;
import com.zhiwei.park.domain.SettleIn;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiwei.park.domain.dto.SettleInApplyDto;
import com.zhiwei.park.domain.vo.SettleInDetailVo;

/**
 * 入驻表Service接口
 * 
 * @author ypf
 * @date 2026-10-30
 */
public interface ISettleInService extends IService<SettleIn>
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
     * 批量删除入驻表
     * 
     * @param ids 需要删除的入驻表主键集合
     * @return 结果
     */
    public int deleteSettleInByIds(Long[] ids);

    /**
     * 删除入驻表信息
     *
     * @param id 入驻表主键
     * @return 结果
     */
    public int deleteSettleInById(Long id);

    /**
     * 申请入驻
     *
     * @param dto 申请入驻信息
     */
    public void applySettleIn(SettleInApplyDto dto);

    /**
     * 查询入驻详情
     *
     * @param id 入驻id
     * @return 入驻详情
     */
    public SettleInDetailVo getSettleInDetail(Long id);
}
