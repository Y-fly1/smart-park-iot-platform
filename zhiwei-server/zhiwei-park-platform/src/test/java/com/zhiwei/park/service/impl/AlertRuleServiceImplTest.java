package com.zhiwei.park.service.impl;

import java.time.LocalTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.zhiwei.common.constant.CacheConstants;
import com.zhiwei.park.domain.AlertData;
import com.zhiwei.park.domain.AlertRule;
import com.zhiwei.park.domain.DeviceData;
import com.zhiwei.park.mapper.AlertRuleMapper;
import com.zhiwei.park.service.IAlertDataService;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 报警规则过滤功能单元测试
 *
 * @author ypf
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AlertRuleServiceImplTest
{
    private static final String IOT_ID = "dev-001";
    private static final String PRODUCT_KEY = "pk-001";
    private static final String FUNCTION_ID = "heartRate";
    private static final String COUNT_KEY = CacheConstants.ALERT_COUNT_KEY + IOT_ID + ":" + FUNCTION_ID + ":1";
    private static final String SILENT_KEY = CacheConstants.ALERT_SILENT_KEY + IOT_ID + ":" + FUNCTION_ID + ":1";

    /** Redis 写入时的JSON格式，需与生产代码保持一致 */
    private static final JSONConfig JSON_CONFIG = JSONConfig.create().setDateFormat("yyyy-MM-dd HH:mm:ss");

    @Mock
    private AlertRuleMapper alertRuleMapper;

    @Mock
    private IAlertDataService alertDataService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private HashOperations hashOperations;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private AlertRuleServiceImpl alertRuleService;

    @BeforeAll
    static void initTableInfo()
    {
        // 单元测试未启动MyBatis，手动初始化实体元数据，保证Lambda条件构造器可用
        MapperBuilderAssistant assistant = new MapperBuilderAssistant(new MybatisConfiguration(), "");
        TableInfoHelper.initTableInfo(assistant, AlertRule.class);
    }

    @BeforeEach
    void setUp()
    {
        alertRuleService = new AlertRuleServiceImpl();
        ReflectionTestUtils.setField(alertRuleService, "baseMapper", alertRuleMapper);
        ReflectionTestUtils.setField(alertRuleService, "alertRuleMapper", alertRuleMapper);
        ReflectionTestUtils.setField(alertRuleService, "alertDataService", alertDataService);
        ReflectionTestUtils.setField(alertRuleService, "redisTemplate", redisTemplate);
        lenient().when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("四层校验全部通过时触发报警并保存报警数据")
    void shouldTriggerAlertWhenAllChecksPass()
    {
        DeviceData reportedData = buildReportedData("60", new Date());
        AlertRule rule = buildRule("<", 65, 1, 5, "00:00:00~23:59:59");
        mockRedisReportedData(reportedData);
        mockRuleQueries(List.of(rule), List.of(rule), Collections.emptyList());
        when(redisTemplate.hasKey(SILENT_KEY)).thenReturn(false);
        when(valueOperations.increment(COUNT_KEY)).thenReturn(1L);
        when(alertDataService.save(any(AlertData.class))).thenReturn(true);

        alertRuleService.alertFilter();

        ArgumentCaptor<AlertData> captor = ArgumentCaptor.forClass(AlertData.class);
        verify(alertDataService).save(captor.capture());
        AlertData saved = captor.getValue();
        assertNotNull(saved);
        assertEquals(IOT_ID, saved.getIotId());
        assertEquals(PRODUCT_KEY, saved.getProductKey());
        assertEquals(FUNCTION_ID, saved.getFunctionId());
        assertEquals(1L, saved.getAlertRuleId().longValue());
        assertEquals(0, saved.getType().intValue());
        assertEquals(0, saved.getStatus().intValue());
        assertEquals("60", saved.getDataValue());
        assertEquals("心率<65.0，持续1个周期", saved.getAlertReason());
        verify(redisTemplate).delete(COUNT_KEY);
        verify(valueOperations).set(eq(SILENT_KEY), anyString(), eq(5L), eq(TimeUnit.MINUTES));
    }

    @Test
    @DisplayName("报警次数达到持续周期时触发报警")
    void shouldTriggerAlertWhenAlertCountReachesDuration()
    {
        DeviceData reportedData = buildReportedData("60", new Date());
        AlertRule rule = buildRule("<", 65, 3, 5, "00:00:00~23:59:59");
        mockRedisReportedData(reportedData);
        mockRuleQueries(List.of(rule), List.of(rule), Collections.emptyList());
        when(redisTemplate.hasKey(SILENT_KEY)).thenReturn(false);
        when(valueOperations.increment(COUNT_KEY)).thenReturn(3L);
        when(alertDataService.save(any(AlertData.class))).thenReturn(true);

        alertRuleService.alertFilter();

        verify(alertDataService).save(any(AlertData.class));
    }

    @Test
    @DisplayName("持续周期未达标时不触发报警")
    void shouldNotTriggerAlertWhenDurationNotReached()
    {
        DeviceData reportedData = buildReportedData("60", new Date());
        AlertRule rule = buildRule("<", 65, 3, 5, "00:00:00~23:59:59");
        mockRedisReportedData(reportedData);
        mockRuleQueries(List.of(rule), List.of(rule), Collections.emptyList());
        when(redisTemplate.hasKey(SILENT_KEY)).thenReturn(false);
        when(valueOperations.increment(COUNT_KEY)).thenReturn(1L);

        alertRuleService.alertFilter();

        verify(alertDataService, never()).save(any(AlertData.class));
        verify(redisTemplate, never()).delete(COUNT_KEY);
    }

    @Test
    @DisplayName("数据不是异常数据时删除报警次数且不触发报警")
    void shouldDeleteAlertCountWhenDataIsNormal()
    {
        DeviceData reportedData = buildReportedData("70", new Date());
        AlertRule rule = buildRule("<", 65, 1, 5, "00:00:00~23:59:59");
        mockRedisReportedData(reportedData);
        mockRuleQueries(List.of(rule), List.of(rule), Collections.emptyList());

        alertRuleService.alertFilter();

        verify(redisTemplate).delete(COUNT_KEY);
        verify(valueOperations, never()).increment(anyString());
        verify(alertDataService, never()).save(any(AlertData.class));
    }

    @Test
    @DisplayName("处于沉默周期内时不触发报警")
    void shouldNotTriggerAlertWhenInSilentPeriod()
    {
        DeviceData reportedData = buildReportedData("60", new Date());
        AlertRule rule = buildRule("<", 65, 1, 5, "00:00:00~23:59:59");
        mockRedisReportedData(reportedData);
        mockRuleQueries(List.of(rule), List.of(rule), Collections.emptyList());
        when(redisTemplate.hasKey(SILENT_KEY)).thenReturn(true);

        alertRuleService.alertFilter();

        verify(valueOperations, never()).increment(anyString());
        verify(alertDataService, never()).save(any(AlertData.class));
    }

    @Test
    @DisplayName("不在生效时段内时不触发报警")
    void shouldNotTriggerAlertWhenOutOfEffectivePeriod()
    {
        DeviceData reportedData = buildReportedData("60", new Date());
        LocalTime now = LocalTime.now().withNano(0);
        String effectivePeriod = now.plusHours(1) + "~" + now.plusHours(2);
        AlertRule rule = buildRule("<", 65, 1, 5, effectivePeriod);
        mockRedisReportedData(reportedData);
        mockRuleQueries(List.of(rule), List.of(rule), Collections.emptyList());

        alertRuleService.alertFilter();

        verify(valueOperations, never()).increment(anyString());
        verify(alertDataService, never()).save(any(AlertData.class));
    }

    @Test
    @DisplayName("上报时间超过1分钟时跳过该条数据")
    void shouldSkipExpiredReportedData()
    {
        DeviceData reportedData = buildReportedData("60", new Date(System.currentTimeMillis() - 2 * 60 * 1000L));
        AlertRule rule = buildRule("<", 65, 1, 5, "00:00:00~23:59:59");
        mockRedisReportedData(reportedData);
        when(alertRuleMapper.selectList(any())).thenReturn(List.of(rule));

        alertRuleService.alertFilter();

        // 只执行了"查询所有可用规则"这一次查询，未进入规则匹配
        verify(alertRuleMapper, times(1)).selectList(any());
        verify(alertDataService, never()).save(any(AlertData.class));
    }

    @Test
    @DisplayName("没有可用报警规则时直接结束")
    void shouldReturnWhenNoEnabledRule()
    {
        when(alertRuleMapper.selectList(any())).thenReturn(Collections.emptyList());

        alertRuleService.alertFilter();

        verify(redisTemplate, never()).opsForHash();
        verify(alertDataService, never()).save(any(AlertData.class));
    }

    @Test
    @DisplayName("Redis中没有设备上报数据时直接结束")
    void shouldReturnWhenNoReportedDataInRedis()
    {
        AlertRule rule = buildRule("<", 65, 1, 5, "00:00:00~23:59:59");
        when(alertRuleMapper.selectList(any())).thenReturn(List.of(rule));
        when(hashOperations.entries(CacheConstants.IOT_DEVICE_DATA_LIST)).thenReturn(Collections.emptyMap());

        alertRuleService.alertFilter();

        verify(alertDataService, never()).save(any(AlertData.class));
    }

    @Test
    @DisplayName("解析后上报数据集合为空时直接结束")
    void shouldReturnWhenParsedReportedDataIsEmpty()
    {
        AlertRule rule = buildRule("<", 65, 1, 5, "00:00:00~23:59:59");
        Map<Object, Object> emptyDataMap = new HashMap<>();
        emptyDataMap.put(IOT_ID, "[]");
        when(alertRuleMapper.selectList(any())).thenReturn(List.of(rule));
        when(hashOperations.entries(CacheConstants.IOT_DEVICE_DATA_LIST)).thenReturn(emptyDataMap);

        alertRuleService.alertFilter();

        verify(alertDataService, never()).save(any(AlertData.class));
    }

    /**
     * 构造设备上报数据
     */
    private DeviceData buildReportedData(String dataValue, Date alarmTime)
    {
        DeviceData data = new DeviceData();
        data.setIotId(IOT_ID);
        data.setDeviceName("心率带001");
        data.setProductKey(PRODUCT_KEY);
        data.setProductName("睡眠监测带");
        data.setFunctionId(FUNCTION_ID);
        data.setAccessLocation("101房间1床");
        data.setLocationType(0);
        data.setPhysicalLocationType(2);
        data.setDeviceDescription("测试设备");
        data.setDataValue(dataValue);
        data.setAlarmTime(alarmTime);
        return data;
    }

    /**
     * 构造报警规则
     */
    private AlertRule buildRule(String operator, double value, int duration, int silentPeriod, String effectivePeriod)
    {
        AlertRule rule = new AlertRule();
        rule.setId(1L);
        rule.setProductKey(PRODUCT_KEY);
        rule.setProductName("睡眠监测带");
        rule.setFunctionId(FUNCTION_ID);
        rule.setFunctionName("心率");
        rule.setIotId("-1");
        rule.setDeviceName("心率带001");
        rule.setAlertDataType(0);
        rule.setAlertRuleName("心率低值报警");
        rule.setOperator(operator);
        rule.setValue(value);
        rule.setDuration(duration);
        rule.setAlertEffectivePeriod(effectivePeriod);
        rule.setAlertSilentPeriod(silentPeriod);
        rule.setStatus(1);
        return rule;
    }

    /**
     * 模拟Redis中的设备上报数据
     */
    private void mockRedisReportedData(DeviceData reportedData)
    {
        String jsonStr = JSONUtil.toJsonStr(Collections.singletonList(reportedData), JSON_CONFIG);
        Map<Object, Object> dataMap = new HashMap<>();
        dataMap.put(reportedData.getIotId(), jsonStr);
        when(hashOperations.entries(CacheConstants.IOT_DEVICE_DATA_LIST)).thenReturn(dataMap);
    }

    /**
     * 模拟规则查询结果：可用规则、通用规则、专属规则
     */
    private void mockRuleQueries(List<AlertRule> allRules, List<AlertRule> commonRules, List<AlertRule> deviceRules)
    {
        when(alertRuleMapper.selectList(any())).thenReturn(allRules, commonRules, deviceRules);
    }
}
