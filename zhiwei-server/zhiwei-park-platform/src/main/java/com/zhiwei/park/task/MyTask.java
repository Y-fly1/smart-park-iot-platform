package com.zhiwei.park.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyTask {

    @Scheduled(cron = "0/5 * * * * ?")
    public void myTask() {
        System.out.println(LocalDateTime.now());
    }
}
