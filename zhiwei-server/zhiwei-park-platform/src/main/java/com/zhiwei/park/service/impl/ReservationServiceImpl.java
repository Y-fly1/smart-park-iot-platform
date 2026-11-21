package com.zhiwei.park.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiwei.common.exception.ServiceException;
import com.zhiwei.park.domain.Reservation;
import com.zhiwei.park.domain.dto.ReservationAddDto;
import com.zhiwei.park.domain.vo.ReservationTimeCountVo;
import com.zhiwei.park.mapper.ReservationMapper;
import com.zhiwei.park.service.IReservationService;
import com.zhiwei.park.util.MemberAuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 预约信息Service业务层处理
 *
 * @author ypf
 * @date 2026-10-30
 */
@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements IReservationService
{
    /** 每天最多取消次数，超过则当天不能预约 */
    private static final int MAX_CANCEL_PER_DAY = 3;

    /** 每个时段最大预约人数 */
    private static final int MAX_CAPACITY_PER_SLOT = 6;

    /** 预约类型：参观预约 */
    private static final int TYPE_VISIT = 0;

    /** 预约状态：待报道 */
    private static final int STATUS_WAITING = 0;

    /** 预约状态：已完成 */
    private static final int STATUS_FINISHED = 1;

    /** 预约状态：取消 */
    private static final int STATUS_CANCELLED = 2;

    /** 预约状态：过期 */
    private static final int STATUS_EXPIRED = 3;

    @Autowired
    private MemberAuthHelper memberAuthHelper;

    /**
     * 查询当前联系人当天取消预约次数
     *
     * @return 取消次数
     */
    @Override
    public int countCancelledToday()
    {
        Long userId = memberAuthHelper.getUserId();
        long count = lambdaQuery()
                .eq(Reservation::getCreateBy, String.valueOf(userId))
                .eq(Reservation::getStatus, STATUS_CANCELLED)
                .apply("date(update_time) = curdate()")
                .count();
        return (int) count;
    }

    /**
     * 查询指定日期各时段剩余预约次数
     *
     * @param time 日期时间戳，为空时取当天
     * @return 时段剩余次数集合
     */
    @Override
    public List<ReservationTimeCountVo> countByTime(Long time)
    {
        LocalDate date = time == null
                ? LocalDate.now()
                : Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate();
        // SQL 返回的 count 是已预约数量，这里换算成剩余可预约次数
        List<ReservationTimeCountVo> list = baseMapper.selectTimeUsedCount(date);
        list.forEach(vo -> vo.setCount(MAX_CAPACITY_PER_SLOT - vo.getCount()));
        return list;
    }

    /**
     * 新增预约
     *
     * @param dto 预约信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReservation(ReservationAddDto dto)
    {
        // 当前只支持参观预约
        if (dto.getType() != null && dto.getType() != TYPE_VISIT)
        {
            throw new ServiceException("暂只支持参观预约");
        }
        if (dto.getTime().isBefore(LocalDateTime.now()))
        {
            throw new ServiceException("预约时间不能早于当前时间");
        }

        Long userId = memberAuthHelper.getUserId();

        // 当天取消超过3次则不允许预约
        if (countCancelledToday() > MAX_CANCEL_PER_DAY)
        {
            throw new ServiceException("当天取消预约次数已达上限，无法预约");
        }

        // 校验时段余量
        long used = lambdaQuery()
                .eq(Reservation::getTime, dto.getTime())
                .in(Reservation::getStatus, STATUS_WAITING, STATUS_FINISHED)
                .count();
        if (used >= MAX_CAPACITY_PER_SLOT)
        {
            throw new ServiceException("该时段预约已满，请选择其他时间");
        }

        // 同一手机号同一时段只保留一条记录（表上有唯一索引）
        Reservation existing = lambdaQuery()
                .eq(Reservation::getMobile, dto.getMobile())
                .eq(Reservation::getTime, dto.getTime())
                .one();
        if (existing != null)
        {
            if (existing.getStatus() != STATUS_CANCELLED)
            {
                throw new ServiceException("该时段您已预约，请勿重复提交");
            }
            // 之前取消过，重新预约时复用原记录
            existing.setName(dto.getName());
            existing.setVisitor(dto.getVisitor());
            existing.setType(TYPE_VISIT);
            existing.setStatus(STATUS_WAITING);
            existing.setUpdateBy(String.valueOf(userId));
            existing.setUpdateTime(new Date());
            updateById(existing);
            return;
        }

        Reservation reservation = new Reservation();
        reservation.setName(dto.getName());
        reservation.setMobile(dto.getMobile());
        reservation.setTime(dto.getTime());
        reservation.setVisitor(dto.getVisitor());
        reservation.setType(TYPE_VISIT);
        reservation.setStatus(STATUS_WAITING);
        reservation.setCreateBy(String.valueOf(userId));
        reservation.setCreateTime(new Date());
        save(reservation);
    }

    /**
     * 分页查询当前联系人的预约列表
     *
     * @param status 预约状态，为空时查全部
     * @return 预约集合
     */
    @Override
    public List<Reservation> selectMemberReservationList(Integer status)
    {
        Long userId = memberAuthHelper.getUserId();
        Reservation query = new Reservation();
        query.setCreateBy(String.valueOf(userId));
        query.setStatus(status);
        return baseMapper.selectReservationList(query);
    }

    /**
     * 取消预约
     *
     * @param id 预约主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelReservation(Long id)
    {
        Reservation reservation = getById(id);
        if (reservation == null)
        {
            throw new ServiceException("预约不存在");
        }
        Long userId = memberAuthHelper.getUserId();
        if (!String.valueOf(userId).equals(reservation.getCreateBy()))
        {
            throw new ServiceException("无权操作该预约");
        }
        if (reservation.getStatus() == null || reservation.getStatus() != STATUS_WAITING)
        {
            throw new ServiceException("当前预约状态不能取消");
        }
        reservation.setStatus(STATUS_CANCELLED);
        reservation.setUpdateBy(String.valueOf(userId));
        reservation.setUpdateTime(new Date());
        updateById(reservation);
    }

    /**
     * 将已超时未报道的预约置为已过期
     *
     * @return 更新条数
     */
    @Override
    public int expireOverdue()
    {
        LambdaUpdateWrapper<Reservation> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Reservation::getStatus, STATUS_WAITING)
                .lt(Reservation::getTime, LocalDateTime.now())
                .set(Reservation::getStatus, STATUS_EXPIRED)
                .set(Reservation::getUpdateTime, new Date());
        return baseMapper.update(null, wrapper);
    }
}
