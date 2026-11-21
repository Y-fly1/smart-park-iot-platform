package com.zhiwei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 智维云 - 智慧园区安防与能耗管理平台启动类
 */
@EnableScheduling
@SpringBootApplication
public class ZhiweiApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(com.zhiwei.ZhiweiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  智维云启动成功   ლ(´ڡ`ლ)ﾞ ");
    }
}
