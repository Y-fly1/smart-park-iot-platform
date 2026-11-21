package com.zhiwei.park.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiwei.park.domain.Reservation;
import com.zhiwei.park.domain.dto.ReservationAddDto;
import com.zhiwei.park.domain.vo.ReservationTimeCountVo;

/**
 * 预约信息Service接口
 *
 * @author ypf
 * @date 2026-10-30
 */
public interface IReservationService extends IService<Reservation>
{
    /**
     * 查询当前联系人当天取消预约次数
     *
     * @return 取消次数
     */
    public int countCancelledToday();

    /**
     * 查询指定日期各时段剩余预约次数
     *
     * @param time 日期时间戳，为空时取当天
     * @return 时段剩余次数集合
     */
    public List<ReservationTimeCountVo> countByTime(Long time);

    /**
     * 新增预约
     *
     * @param dto 预约信息
     */
    public void addReservation(ReservationAddDto dto);

    /**
     * 分页查询当前联系人的预约列表
     *
     * @param status 预约状态，为空时查全部
     * @return 预约集合
     */
    public List<Reservation> selectMemberReservationList(Integer status);

    /**
     * 取消预约
     *
     * @param id 预约主键
     */
    public void cancelReservation(Long id);

    /**
     * 将已超时未报道的预约置为已过期
     *
     * @return 更新条数
     */
    public int expireOverdue();
}
