package com.zhiwei.park.job;

import cn.hutool.core.collection.CollUtil;
import com.zhiwei.park.domain.Contract;
import com.zhiwei.park.service.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 更新合同状态的定时任务
 *
 * @Author mr.wu
 * @Date 2026-8-20 9:59
 */
@Component("contractJob")
public class UpdateContractStatusJob {

    @Autowired
    private IContractService contractService;

    /**
     * 定时任务
     */
    public void updateStatus(){
        System.out.println("更新合同状态的定时任务开始执行");

        contractService.updateContractStatus();

        System.out.println("更新合同状态的定时任务结束执行");
    }
}