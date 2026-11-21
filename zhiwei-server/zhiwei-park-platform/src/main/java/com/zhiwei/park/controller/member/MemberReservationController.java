package com.zhiwei.park.controller.member;

import java.util.List;
import com.zhiwei.common.core.controller.BaseController;
import com.zhiwei.common.core.domain.AjaxResult;
import com.zhiwei.park.domain.Reservation;
import com.zhiwei.park.domain.dto.ReservationAddDto;
import com.zhiwei.park.service.IReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序预约Controller
 *
 * @author ypf
 * @date 2026-10-30
 */
@RestController
@RequestMapping("/member/reservation")
@Tag(name = "小程序预约相关接口")
public class MemberReservationController extends BaseController
{
    @Autowired
    private IReservationService reservationService;

    /**
     * 查询取消预约数量
     */
    @GetMapping("/cancelled-count")
    @Operation(summary = "查询取消预约数量")
    public AjaxResult cancelledCount()
    {
        return success(reservationService.countCancelledToday());
    }

    /**
     * 查询每个时间段剩余预约次数
     */
    @GetMapping("/countByTime")
    @Operation(summary = "查询每个时间段剩余预约次数")
    public AjaxResult countByTime(@RequestParam(value = "time", required = false) Long time)
    {
        return success(reservationService.countByTime(time));
    }

    /**
     * 新增预约
     */
    @PostMapping({"", "/"})
    @Operation(summary = "新增预约")
    public AjaxResult add(@Validated @RequestBody ReservationAddDto dto)
    {
        reservationService.addReservation(dto);
        return success();
    }

    /**
     * 分页查询预约
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询预约")
    public AjaxResult page(@RequestParam(value = "status", required = false) Integer status)
    {
        startPage();
        List<Reservation> list = reservationService.selectMemberReservationList(status);
        return success(getDataTable(list));
    }

    /**
     * 取消预约
     */
    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消预约")
    public AjaxResult cancel(@Schema(name = "预约ID", requiredMode = Schema.RequiredMode.REQUIRED)
                             @PathVariable("id") Long id)
    {
        reservationService.cancelReservation(id);
        return success();
    }
}
