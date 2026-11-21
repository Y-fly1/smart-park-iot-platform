package com.zhiwei.common.constant;

/**
 * 缓存的key 常量
 * 
 * @author ruoyi
 */
public class CacheConstants
{
    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";


    public static final String IOT_ALL_PRODUCT_LIST= "iot:all_product_list";

    /**
     * 设备最新上报数据 Hash 的 key，小 key 为设备ID
     */
    public static final String IOT_DEVICE_DATA_LIST = "iot:data:list";

    /**
     * 报警次数 redis key 前缀，完整 key 为：前缀 + 设备ID + 功能标识 + 规则ID
     */
    public static final String ALERT_COUNT_KEY = "alert:count:";

    /**
     * 报警沉默周期 redis key 前缀，完整 key 为：前缀 + 设备ID + 功能标识 + 规则ID
     */
    public static final String ALERT_SILENT_KEY = "alert:silent:";
}
