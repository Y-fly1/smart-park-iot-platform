package com.zhiwei.park.task;

import com.zhiwei.park.service.IReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 预约过期定时任务
 *
 * @author ypf
 * @date 2026-10-30
 */
@Component
public class ReservationExpireTask
{
    private static final Logger log = LoggerFactory.getLogger(ReservationExpireTask.class);

    @Autowired
    private IReservationService reservationService;

    /**
     * 每小时的第1分钟和第31分钟检查超时未报道的预约
     */
    @Scheduled(cron = "0 1,31 * * * ?")
    public void expireOverdueReservation()
    {
        int count = reservationService.expireOverdue();
        log.info("预约过期检查完成，本次置为过期 {} 条", count);
    }
}
