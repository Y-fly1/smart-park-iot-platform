package com.zhiwei.park.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zhiwei.common.constant.CacheConstants;
import com.zhiwei.framework.config.FastJson2JsonRedisSerializer;
import com.zhiwei.park.domain.AlertData;
import com.zhiwei.park.domain.AlertRule;
import com.zhiwei.park.domain.DeviceData;
import com.zhiwei.park.mapper.AlertRuleMapper;
import com.zhiwei.park.service.IAlertDataService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 报警规则过滤端到端测试
 * 连接真实MySQL与Redis（使用独立的Redis DB 15），执行完成后自动清理测试数据。
 * <p>
 * 默认不执行，需显式设置环境变量：ALERT_E2E=true
 * </p>
 *
 * @author ypf
 */
@EnabledIfEnvironmentVariable(named = "ALERT_E2E", matches = "true")
@SpringBootTest(classes = AlertFilterE2ETest.TestApp.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AlertFilterE2ETest
{
    /** 测试环境连接信息统一从环境变量读取，未配置时使用本地默认值 */
    private static final String MYSQL_URL = "jdbc:mysql://" + env("DB_HOST", "localhost") + ":" + env("DB_PORT", "3306")
            + "/" + env("DB_NAME", "zhiwei")
            + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
    private static final String MYSQL_USERNAME = env("DB_USERNAME", "root");
    private static final String MYSQL_PASSWORD = env("DB_PASSWORD", "");

    private static final String REDIS_HOST = env("REDIS_HOST", "localhost");
    private static final int REDIS_PORT = Integer.parseInt(env("REDIS_PORT", "6379"));
    private static final String REDIS_PASSWORD = env("REDIS_PASSWORD", "");
    /** 使用独立的Redis库，避免影响生产DB 0中的数据 */
    private static final int REDIS_DATABASE = 15;

    /**
     * 读取环境变量，未设置或为空时返回默认值
     */
    private static String env(String key, String defaultValue)
    {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    /** 与生产代码一致的JSON时间格式 */
    private static final JSONConfig JSON_CONFIG = JSONConfig.create().setDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private AlertRuleServiceImpl alertRuleService;

    @Autowired
    private IAlertDataService alertDataService;

    @Autowired
    private AlertRuleMapper alertRuleMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private Long ruleId;
    private String iotId;
    private String countKey;
    private String silentKey;

    @Test
    @DisplayName("端到端：真实MySQL+Redis下异常数据触发报警并写入alert_data")
    void shouldTriggerAlertEndToEnd()
    {
        String suffix = String.valueOf(System.currentTimeMillis());
        iotId = "e2e-dev-" + suffix;
        String productKey = "e2e-pk-" + suffix;
        String functionId = "e2e-hr-" + suffix;
        countKey = CacheConstants.ALERT_COUNT_KEY + iotId + ":" + functionId + ":";
        silentKey = CacheConstants.ALERT_SILENT_KEY + iotId + ":" + functionId + ":";

        // 1. 插入一条测试报警规则：心率 < 65，持续1个周期，沉默5分钟
        AlertRule rule = new AlertRule();
        rule.setProductKey(productKey);
        rule.setProductName("端到端测试产品");
        rule.setFunctionId(functionId);
        rule.setFunctionName("端到端心率");
        rule.setIotId("-1");
        rule.setDeviceName("端到端测试设备");
        rule.setAlertDataType(0);
        rule.setAlertRuleName("端到端心率低值报警");
        rule.setOperator("<");
        rule.setValue(65D);
        rule.setDuration(1);
        rule.setAlertEffectivePeriod("00:00:00~23:59:59");
        rule.setAlertSilentPeriod(5);
        rule.setStatus(1);
        rule.setCreateTime(new Date());
        alertRuleMapper.insert(rule);
        ruleId = rule.getId();
        countKey = countKey + ruleId;
        silentKey = silentKey + ruleId;
        assertNotNull(ruleId);

        // 2. 按生产写入格式，将设备上报数据写入Redis Hash
        DeviceData reportedData = new DeviceData();
        reportedData.setIotId(iotId);
        reportedData.setDeviceName("端到端测试设备");
        reportedData.setProductKey(productKey);
        reportedData.setProductName("端到端测试产品");
        reportedData.setFunctionId(functionId);
        reportedData.setAccessLocation("E2E测试位置");
        reportedData.setLocationType(0);
        reportedData.setPhysicalLocationType(2);
        reportedData.setDeviceDescription("E2E测试");
        reportedData.setDataValue("60");
        reportedData.setAlarmTime(new Date());
        String jsonStr = JSONUtil.toJsonStr(Collections.singletonList(reportedData), JSON_CONFIG);
        redisTemplate.opsForHash().put(CacheConstants.IOT_DEVICE_DATA_LIST, iotId, jsonStr);

        // 3. 执行报警过滤
        alertRuleService.alertFilter();

        // 4. 校验alert_data表已写入报警数据
        List<AlertData> alertDataList = alertDataService.list(
                Wrappers.<AlertData>lambdaQuery().eq(AlertData::getAlertRuleId, ruleId));
        assertEquals(1, alertDataList.size());
        AlertData alertData = alertDataList.get(0);
        assertEquals(iotId, alertData.getIotId());
        assertEquals(productKey, alertData.getProductKey());
        assertEquals(functionId, alertData.getFunctionId());
        assertEquals("60", alertData.getDataValue());
        assertEquals(0, alertData.getType().intValue());
        assertEquals(0, alertData.getStatus().intValue());
        assertEquals("端到端心率<65.0，持续1个周期", alertData.getAlertReason());

        // 5. 校验Redis：报警次数已删除、沉默周期已写入且有效期约5分钟
        assertFalse(Boolean.TRUE.equals(redisTemplate.hasKey(countKey)));
        assertTrue(Boolean.TRUE.equals(redisTemplate.hasKey(silentKey)));
        Long ttl = redisTemplate.getExpire(silentKey, TimeUnit.SECONDS);
        assertNotNull(ttl);
        assertTrue(ttl > 0 && ttl <= 300, "沉默周期TTL应在0~300秒之间，实际：" + ttl);
    }

    @AfterEach
    void cleanUp()
    {
        try {
            if (ruleId != null) {
                alertDataService.remove(Wrappers.<AlertData>lambdaQuery().eq(AlertData::getAlertRuleId, ruleId));
                alertRuleMapper.deleteById(ruleId);
            }
            if (iotId != null) {
                redisTemplate.opsForHash().delete(CacheConstants.IOT_DEVICE_DATA_LIST, iotId);
            }
            if (countKey != null) {
                redisTemplate.delete(countKey);
            }
            if (silentKey != null) {
                redisTemplate.delete(silentKey);
            }
        } catch (Exception e) {
            System.err.println("E2E测试数据清理失败：" + e.getMessage());
        }
    }

    /**
     * 测试专用最小上下文：只装配数据源、MyBatis-Plus、Redis与报警服务，避免加载整个若依应用
     */
    @SpringBootConfiguration
    @MapperScan("com.zhiwei.park.mapper")
    @Import({AlertRuleServiceImpl.class, AlertDataServiceImpl.class})
    static class TestApp
    {
        @Bean
        public DriverManagerDataSource dataSource()
        {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl(MYSQL_URL);
            dataSource.setUsername(MYSQL_USERNAME);
            dataSource.setPassword(MYSQL_PASSWORD);
            return dataSource;
        }

        @Bean
        public MybatisSqlSessionFactoryBean sqlSessionFactory(DriverManagerDataSource dataSource,
                                                              MetaObjectHandler metaObjectHandler)
        {
            MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
            factoryBean.setDataSource(dataSource);

            MybatisConfiguration configuration = new MybatisConfiguration();
            configuration.setMapUnderscoreToCamelCase(true);
            factoryBean.setConfiguration(configuration);

            GlobalConfig globalConfig = new GlobalConfig();
            globalConfig.setDbConfig(new GlobalConfig.DbConfig().setIdType(IdType.AUTO));
            globalConfig.setMetaObjectHandler(metaObjectHandler);
            factoryBean.setGlobalConfig(globalConfig);
            return factoryBean;
        }

        /**
         * 模拟生产环境的公共字段自动填充：create_time / update_time
         */
        @Bean
        public MetaObjectHandler metaObjectHandler()
        {
            return new MetaObjectHandler()
            {
                @Override
                public void insertFill(org.apache.ibatis.reflection.MetaObject metaObject)
                {
                    this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
                }

                @Override
                public void updateFill(org.apache.ibatis.reflection.MetaObject metaObject)
                {
                    this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
                }
            };
        }

        @Bean
        public LettuceConnectionFactory redisConnectionFactory()
        {
            RedisStandaloneConfiguration configuration =
                    new RedisStandaloneConfiguration(REDIS_HOST, REDIS_PORT);
            // 无密码的本地 Redis 不设置密码，避免发送空 AUTH 导致连接失败
            if (!REDIS_PASSWORD.isBlank())
            {
                configuration.setPassword(RedisPassword.of(REDIS_PASSWORD));
            }
            configuration.setDatabase(REDIS_DATABASE);
            LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration);
            connectionFactory.afterPropertiesSet();
            return connectionFactory;
        }

        @Bean
        @SuppressWarnings({"rawtypes", "unchecked"})
        public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory)
        {
            RedisTemplate<String, String> template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory);

            FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);
            template.setKeySerializer(new StringRedisSerializer());
            template.setValueSerializer(serializer);
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setHashValueSerializer(serializer);

            template.afterPropertiesSet();
            return template;
        }
    }
}
