package com.zhiwei.park.mapper;

import java.time.LocalDate;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiwei.park.domain.Reservation;
import com.zhiwei.park.domain.vo.ReservationTimeCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 预约信息Mapper接口
 *
 * @author ypf
 * @date 2026-10-30
 */
@Mapper
public interface ReservationMapper extends BaseMapper<Reservation>
{
    /**
     * 查询预约列表
     *
     * @param reservation 查询条件（createBy、status）
     * @return 预约集合
     */
    List<Reservation> selectReservationList(Reservation reservation);

    /**
     * 统计指定日期各时段已预约数量
     *
     * @param date 日期
     * @return 时段及已预约数量
     */
    List<ReservationTimeCountVo> selectTimeUsedCount(@Param("date") LocalDate date);
}
