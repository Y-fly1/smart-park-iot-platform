package com.zhiwei.park.service.impl;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.zhiwei.common.constant.CacheConstants;
import com.zhiwei.common.utils.StringUtils;
import com.zhiwei.park.mapper.AlertDataMapper;
import com.zhiwei.park.mapper.AlertRuleMapper;
import com.zhiwei.park.domain.AlertRule;
import com.zhiwei.park.domain.AlertData;
import com.zhiwei.park.domain.DeviceData;
import com.zhiwei.park.service.IAlertRuleService;
import com.zhiwei.park.service.IAlertDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Arrays;

/**
 * 报警规则Service业务层处理
 *
 * @author ypf
 * @date 2026-11-05
 */
@Slf4j
@Service
public class AlertRuleServiceImpl extends ServiceImpl<AlertRuleMapper, AlertRule> implements IAlertRuleService
{
    /** 通用规则标识，iot_id 为该值表示规则作用于对应产品下的全部设备 */
    private static final String ALL_DEVICE_IOT_ID = "-1";

    /** 上报数据有效时长（毫秒），超过该时长不参与报警过滤 */
    private static final long DATA_VALID_MILLIS = 60 * 1000L;

    /** 报警生效时段分隔符 */
    private static final String PERIOD_SEPARATOR = "~";

    /** 生效时段格式，支持 00:00:00 与 00:00 两种写法 */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm[:ss]");

    /** Redis 设备上报数据的 JSON 时间格式，需与写入方保持一致 */
    private static final JSONConfig JSON_CONFIG = JSONConfig.create().setDateFormat("yyyy-MM-dd HH:mm:ss");

    /** 报警数据类型：0 企业异常数据，1 设备异常数据 */
    private static final int ALERT_TYPE_TENANT_ABNORMAL = 0;
    private static final int ALERT_TYPE_DEVICE_ABNORMAL = 1;

    /** 设备位置类型：0 随身设备，1 固定设备 */
    private static final int LOCATION_TYPE_MOBILE = 0;
    private static final int LOCATION_TYPE_FIXED = 1;

    /** 报警接收角色名称 */
    private static final String ROLE_ADMINISTRATIVE = "行政";
    private static final String ROLE_SUPER_ADMIN = "超级管理员";

    @Autowired
    private AlertRuleMapper alertRuleMapper;

    @Autowired
    private AlertDataMapper alertDataMapper;

    @Autowired
    private IAlertDataService alertDataService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 查询报警规则
     *
     * @param id 报警规则主键
     * @return 报警规则
     */
    @Override
    public AlertRule selectAlertRuleById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询报警规则列表
     *
     * @param alertRule 报警规则
     * @return 报警规则
     */
    @Override
    public List<AlertRule> selectAlertRuleList(AlertRule alertRule)
    {
        return alertRuleMapper.selectAlertRuleList(alertRule);
    }

    /**
     * 新增报警规则
     *
     * @param alertRule 报警规则
     * @return 结果
     */
    @Override
    public int insertAlertRule(AlertRule alertRule)
    {
        return save(alertRule)? 1 : 0;
    }

    /**
     * 修改报警规则
     *
     * @param alertRule 报警规则
     * @return 结果
     */
    @Override
    public int updateAlertRule(AlertRule alertRule)
    {
        return updateById(alertRule)? 1 : 0;
    }

    /**
     * 批量删除报警规则
     *
     * @param ids 需要删除的报警规则主键
     * @return 结果
     */
    @Override
    public int deleteAlertRuleByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除报警规则信息
     *
     * @param id 报警规则主键
     * @return 结果
     */
    @Override
    public int deleteAlertRuleById(Long id)
    {
        return removeById(id)? 1 : 0;
    }

    /**
     * 设备上报数据报警过滤
     */
    @Override
    public void alertFilter()
    {
        log.info("设备上报数据报警过滤开始");

        // 一、前置数据判断，避免无效数据
        // 1. 查询所有可用报警规则
        List<AlertRule> allRules = alertRuleMapper.selectList(
                Wrappers.<AlertRule>lambdaQuery().eq(AlertRule::getStatus, 1));
        if (CollUtil.isEmpty(allRules)) {
            log.error("报警过滤结束：未查询到可用的报警规则");
            return;
        }

        // 2. 查询Redis Hash中的所有设备上报数据
        Map<Object, Object> deviceDataMap = redisTemplate.opsForHash().entries(CacheConstants.IOT_DEVICE_DATA_LIST);
        if (CollUtil.isEmpty(deviceDataMap)) {
            log.error("报警过滤结束：Redis中未查询到设备上报数据");
            return;
        }

        // 3. 汇总Hash中的所有value到上报数据集合
        List<DeviceData> reportedDataList = parseReportedData(deviceDataMap);

        // 4. 上报数据集合为空则直接结束
        if (CollUtil.isEmpty(reportedDataList)) {
            log.error("报警过滤结束：设备上报数据集合为空");
            return;
        }
        log.info("报警过滤：可用报警规则{}条，设备上报数据{}条", allRules.size(), reportedDataList.size());

        // 二、遍历设备上报数据，获取报警规则并进行校验
        for (DeviceData reportedData : reportedDataList) {
            try {
                filterDeviceData(reportedData);
            } catch (Exception e) {
                log.error("报警过滤异常，设备ID：{}，功能标识：{}", reportedData.getIotId(), reportedData.getFunctionId(), e);
            }
        }
        log.info("设备上报数据报警过滤结束");
    }

    /**
     * 对单条设备上报数据进行报警过滤
     *
     * @param reportedData 设备上报数据
     */
    private void filterDeviceData(DeviceData reportedData)
    {
        // 二、1. 上报时间超过1分钟的数据不再参与过滤
        if (reportedData.getAlarmTime() == null) {
            log.error("报警过滤跳过：上报数据的上报时间为空，设备ID：{}，功能标识：{}",
                    reportedData.getIotId(), reportedData.getFunctionId());
            return;
        }
        if (System.currentTimeMillis() - reportedData.getAlarmTime().getTime() > DATA_VALID_MILLIS) {
            log.error("报警过滤跳过：上报数据已超过1分钟，设备ID：{}，功能标识：{}，上报时间：{}",
                    reportedData.getIotId(), reportedData.getFunctionId(), reportedData.getAlarmTime());
            return;
        }

        // 二、2. 查询满足要求的报警规则并去重
        Set<AlertRule> ruleSet = queryAlertRules(reportedData);
        if (CollUtil.isEmpty(ruleSet)) {
            log.error("报警过滤跳过：未查询到匹配的报警规则，设备ID：{}，产品key：{}，功能标识：{}",
                    reportedData.getIotId(), reportedData.getProductKey(), reportedData.getFunctionId());
            return;
        }

        // 三、遍历报警规则数据进行4层校验
        for (AlertRule rule : ruleSet) {
            String countKey = buildAlertCountKey(reportedData, rule);
            String silentKey = buildAlertSilentKey(reportedData, rule);

            // 第一层校验：判断生效时间
            if (!isInEffectivePeriod(rule.getAlertEffectivePeriod())) {
                log.error("报警过滤跳过：不在生效时段内，设备ID：{}，规则ID：{}，生效时段：{}",
                        reportedData.getIotId(), rule.getId(), rule.getAlertEffectivePeriod());
                continue;
            }

            // 第二层校验：判断数据阈值，非异常数据删除报警次数后跳过
            if (!isAbnormal(reportedData, rule)) {
                redisTemplate.delete(countKey);
                log.error("报警过滤跳过：上报数据不是异常数据，已删除报警次数，设备ID：{}，规则ID：{}，数据值：{}，运算符：{}，阈值：{}",
                        reportedData.getIotId(), rule.getId(), reportedData.getDataValue(), rule.getOperator(), rule.getValue());
                continue;
            }

            // 第三层校验：判断沉默周期
            if (Boolean.TRUE.equals(redisTemplate.hasKey(silentKey))) {
                log.error("报警过滤跳过：规则处于沉默周期内，设备ID：{}，规则ID：{}", reportedData.getIotId(), rule.getId());
                continue;
            }

            // 第四层校验：判断持续周期
            Long alertCount = redisTemplate.opsForValue().increment(countKey);
            int duration = rule.getDuration() == null ? 1 : rule.getDuration();
            if (alertCount == null || alertCount.longValue() != duration) {
                log.error("报警过滤跳过：持续周期未达标，设备ID：{}，规则ID：{}，当前报警次数：{}，持续周期：{}",
                        reportedData.getIotId(), rule.getId(), alertCount, duration);
                continue;
            }

            // 四、校验全部通过，触发报警
            triggerAlert(reportedData, rule, countKey, silentKey);
        }
    }

    /**
     * 查询上报数据匹配的报警规则
     * 包含产品下的通用规则（iot_id 为 -1）和当前设备的专属规则
     *
     * @param reportedData 设备上报数据
     * @return 去重后的报警规则集合
     */
    private Set<AlertRule> queryAlertRules(DeviceData reportedData)
    {
        Set<AlertRule> ruleSet = new HashSet<>();

        // 2.1 查询同类型产品下的全部通用报警规则
        List<AlertRule> commonRules = alertRuleMapper.selectList(Wrappers.<AlertRule>lambdaQuery()
                .eq(AlertRule::getProductKey, reportedData.getProductKey())
                .eq(AlertRule::getFunctionId, reportedData.getFunctionId())
                .eq(AlertRule::getIotId, ALL_DEVICE_IOT_ID)
                .eq(AlertRule::getStatus, 1));
        if (CollUtil.isNotEmpty(commonRules)) {
            ruleSet.addAll(commonRules);
        }

        // 2.2 查询当前设备对应的专属报警规则
        List<AlertRule> deviceRules = alertRuleMapper.selectList(Wrappers.<AlertRule>lambdaQuery()
                .eq(AlertRule::getProductKey, reportedData.getProductKey())
                .eq(AlertRule::getFunctionId, reportedData.getFunctionId())
                .eq(AlertRule::getIotId, reportedData.getIotId())
                .eq(AlertRule::getStatus, 1));
        if (CollUtil.isNotEmpty(deviceRules)) {
            ruleSet.addAll(deviceRules);
        }
        return ruleSet;
    }

    /**
     * 解析Redis Hash中的设备上报数据
     *
     * @param deviceDataMap Redis Hash数据，大key为设备ID，value为上报数据JSON
     * @return 设备上报数据集合
     */
    private List<DeviceData> parseReportedData(Map<Object, Object> deviceDataMap)
    {
        List<DeviceData> reportedDataList = new ArrayList<>();
        for (Object value : deviceDataMap.values()) {
            if (value == null) {
                continue;
            }
            String jsonStr = value instanceof String ? (String) value : JSONUtil.toJsonStr(value);
            try {
                List<DeviceData> dataList = JSONUtil.parseArray(jsonStr, JSON_CONFIG).toList(DeviceData.class);
                if (CollUtil.isNotEmpty(dataList)) {
                    reportedDataList.addAll(dataList);
                }
            } catch (Exception e) {
                log.error("设备上报数据解析失败，原始数据：{}", jsonStr, e);
            }
        }
        return reportedDataList;
    }

    /**
     * 校验当前时间是否在报警生效时段内
     *
     * @param effectivePeriod 生效时段，格式：00:00:00~23:59:59
     * @return true：在时段内
     */
    private boolean isInEffectivePeriod(String effectivePeriod)
    {
        // 未配置生效时段时默认全天生效
        if (StringUtils.isEmpty(effectivePeriod)) {
            return true;
        }
        String[] times = effectivePeriod.split(PERIOD_SEPARATOR);
        if (times.length != 2) {
            log.error("报警生效时段格式错误：{}", effectivePeriod);
            return false;
        }
        LocalTime startTime = parseLocalTime(times[0]);
        LocalTime endTime = parseLocalTime(times[1]);
        if (startTime == null || endTime == null) {
            log.error("报警生效时段格式错误：{}", effectivePeriod);
            return false;
        }
        LocalTime now = LocalTime.now().withNano(0);
        // 跨天时段，如 22:00:00~06:00:00
        if (startTime.isAfter(endTime)) {
            return !now.isBefore(startTime) || !now.isAfter(endTime);
        }
        return !now.isBefore(startTime) && !now.isAfter(endTime);
    }

    /**
     * 解析时间字符串
     *
     * @param time 时间字符串，如 00:00:00 或 00:00
     * @return 解析失败返回null
     */
    private LocalTime parseLocalTime(String time)
    {
        if (StringUtils.isEmpty(time)) {
            return null;
        }
        try {
            return LocalTime.parse(time.trim(), TIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断上报数据是否异常
     *
     * @param reportedData 设备上报数据
     * @param rule 报警规则
     * @return true：异常数据
     */
    private boolean isAbnormal(DeviceData reportedData, AlertRule rule)
    {
        if (StringUtils.isEmpty(reportedData.getDataValue()) || StringUtils.isEmpty(rule.getOperator()) || rule.getValue() == null) {
            log.error("报警过滤跳过：缺少数据值、运算符或阈值，设备ID：{}，规则ID：{}", reportedData.getIotId(), rule.getId());
            return false;
        }
        double dataValue;
        try {
            dataValue = Double.parseDouble(reportedData.getDataValue());
        } catch (NumberFormatException e) {
            log.error("报警过滤跳过：上报数据值[{}]不是有效数字，设备ID：{}，规则ID：{}",
                    reportedData.getDataValue(), reportedData.getIotId(), rule.getId());
            return false;
        }
        double threshold = rule.getValue();
        switch (rule.getOperator()) {
            case ">":
                return dataValue > threshold;
            case ">=":
                return dataValue >= threshold;
            case "<":
                return dataValue < threshold;
            case "<=":
                return dataValue <= threshold;
            case "=":
            case "==":
                return Double.compare(dataValue, threshold) == 0;
            case "!=":
                return Double.compare(dataValue, threshold) != 0;
            default:
                log.error("报警过滤跳过：不支持的运算符[{}]，规则ID：{}", rule.getOperator(), rule.getId());
                return false;
        }
    }

    /**
     * 触发报警
     *
     * @param reportedData 设备上报数据
     * @param rule 报警规则
     * @param countKey 报警次数redis key
     * @param silentKey 沉默周期redis key
     */
    private void triggerAlert(DeviceData reportedData, AlertRule rule, String countKey, String silentKey)
    {
        // 四、1. 删除报警次数
        redisTemplate.delete(countKey);

        // 四、2. 添加沉默周期
        int silentPeriod = rule.getAlertSilentPeriod() == null ? 0 : rule.getAlertSilentPeriod();
        if (silentPeriod > 0) {
            redisTemplate.opsForValue().set(silentKey, String.valueOf(System.currentTimeMillis()), silentPeriod, TimeUnit.MINUTES);
        }

        // 四、3. 查询符合要求的报警接收用户ID，为空则不再保存报警数据
        Set<Long> userIds = queryAlertUserIds(reportedData, rule);
        if (CollUtil.isEmpty(userIds)) {
            log.error("触发报警跳过：未查询到符合要求的接收用户，设备ID：{}，规则ID：{}，报警数据类型：{}",
                    reportedData.getIotId(), rule.getId(), rule.getAlertDataType());
            return;
        }

        // 四、4. 按接收用户批量构建并保存报警数据到 alert_data 表
        List<AlertData> alertDataList = buildAlertDataList(reportedData, rule, userIds);
        if (alertDataService.saveBatch(alertDataList)) {
            log.info("触发报警：报警数据批量保存成功，设备ID：{}，规则ID：{}，报警数据{}条，接收用户：{}，报警原因：{}",
                    reportedData.getIotId(), rule.getId(), alertDataList.size(), userIds,
                    alertDataList.get(0).getAlertReason());
        } else {
            log.error("触发报警：报警数据批量保存失败，设备ID：{}，规则ID：{}，接收用户：{}",
                    reportedData.getIotId(), rule.getId(), userIds);
        }
    }

    /**
     * 查询符合报警规则的接收用户ID
     * 设备异常数据接收人为行政，企业异常数据接收人为设备绑定企业的巡检员，超级管理员始终接收
     *
     * @param reportedData 设备上报数据
     * @param rule 报警规则
     * @return 去重后的用户ID集合
     */
    private Set<Long> queryAlertUserIds(DeviceData reportedData, AlertRule rule)
    {
        Set<Long> userIds = new HashSet<>();
        Integer alertDataType = rule.getAlertDataType();
        if (alertDataType == null) {
            log.error("查询报警接收用户跳过：报警规则未配置报警数据类型，设备ID：{}，规则ID：{}",
                    reportedData.getIotId(), rule.getId());
            return userIds;
        }

        if (alertDataType == ALERT_TYPE_DEVICE_ABNORMAL) {
            // 设备异常数据：接收人为行政
            List<Long> administrativeUserIds = alertDataMapper.selectUserIdsByRoleName(ROLE_ADMINISTRATIVE);
            userIds.addAll(administrativeUserIds);
            log.info("查询报警接收用户：设备异常数据，行政用户{}个，设备ID：{}，规则ID：{}",
                    administrativeUserIds.size(), reportedData.getIotId(), rule.getId());
        } else if (alertDataType == ALERT_TYPE_TENANT_ABNORMAL) {
            // 企业异常数据：接收人为设备绑定企业的巡检员
            Integer locationType = reportedData.getLocationType();
            List<Long> inspectionUserIds = new ArrayList<>();
            if (Integer.valueOf(LOCATION_TYPE_MOBILE).equals(locationType)) {
                inspectionUserIds = alertDataMapper.selectInspectionUserIdsByMobileDevice(reportedData.getIotId());
            } else if (Integer.valueOf(LOCATION_TYPE_FIXED).equals(locationType)) {
                inspectionUserIds = alertDataMapper.selectInspectionUserIdsByFixedDevice(reportedData.getIotId());
            } else {
                log.error("查询报警接收用户跳过：设备位置类型不支持，设备ID：{}，规则ID：{}，位置类型：{}",
                        reportedData.getIotId(), rule.getId(), locationType);
            }
            userIds.addAll(inspectionUserIds);
            log.info("查询报警接收用户：企业异常数据，巡检员{}个，设备ID：{}，规则ID：{}，位置类型：{}",
                    inspectionUserIds.size(), reportedData.getIotId(), rule.getId(), locationType);
        } else {
            log.error("查询报警接收用户跳过：不支持的报警数据类型[{}]，设备ID：{}，规则ID：{}",
                    alertDataType, reportedData.getIotId(), rule.getId());
            return userIds;
        }

        // 无论哪种报警数据类型，超级管理员都需要接收
        List<Long> superAdminUserIds = alertDataMapper.selectUserIdsByRoleName(ROLE_SUPER_ADMIN);
        userIds.addAll(superAdminUserIds);
        // 剔除角色关联查询可能产生的空值
        userIds.remove(null);
        log.info("查询报警接收用户完成：接收用户{}个，其中超级管理员{}个，设备ID：{}，规则ID：{}",
                userIds.size(), superAdminUserIds.size(), reportedData.getIotId(), rule.getId());
        return userIds;
    }

    /**
     * 按接收用户批量组装报警数据
     *
     * @param reportedData 设备上报数据
     * @param rule 报警规则
     * @param userIds 接收用户ID集合
     * @return 报警数据集合
     */
    private List<AlertData> buildAlertDataList(DeviceData reportedData, AlertRule rule, Set<Long> userIds)
    {
        String alertReason = buildAlertReason(rule);
        List<AlertData> alertDataList = new ArrayList<>(userIds.size());
        for (Long userId : userIds) {
            alertDataList.add(buildAlertData(reportedData, rule, userId, alertReason));
        }
        return alertDataList;
    }

    /**
     * 组装报警数据
     *
     * @param reportedData 设备上报数据
     * @param rule 报警规则
     * @param userId 接收用户ID
     * @param alertReason 报警原因
     * @return 报警数据
     */
    private AlertData buildAlertData(DeviceData reportedData, AlertRule rule, Long userId, String alertReason)
    {
        AlertData alertData = new AlertData();
        alertData.setIotId(reportedData.getIotId());
        alertData.setDeviceName(reportedData.getDeviceName());
        alertData.setProductKey(reportedData.getProductKey());
        alertData.setProductName(reportedData.getProductName());
        alertData.setFunctionId(reportedData.getFunctionId());
        alertData.setAccessLocation(reportedData.getAccessLocation());
        alertData.setLocationType(reportedData.getLocationType());
        alertData.setPhysicalLocationType(reportedData.getPhysicalLocationType());
        alertData.setDeviceDescription(reportedData.getDeviceDescription());
        alertData.setDataValue(reportedData.getDataValue());
        alertData.setAlertRuleId(rule.getId());
        alertData.setAlertReason(alertReason);
        alertData.setType(rule.getAlertDataType());
        // 状态：0 待处理
        alertData.setStatus(0);
        alertData.setUserId(userId);
        return alertData;
    }

    /**
     * 组装报警原因，格式：功能名称+运算符+阈值+持续周期
     *
     * @param rule 报警规则
     * @return 报警原因
     */
    private String buildAlertReason(AlertRule rule)
    {
        String functionName = StringUtils.isNotEmpty(rule.getFunctionName()) ? rule.getFunctionName() : rule.getFunctionId();
        int duration = rule.getDuration() == null ? 1 : rule.getDuration();
        return functionName + rule.getOperator() + rule.getValue() + "，持续" + duration + "个周期";
    }

    /**
     * 构建报警次数redis key
     *
     * @param reportedData 设备上报数据
     * @param rule 报警规则
     * @return redis key
     */
    private String buildAlertCountKey(DeviceData reportedData, AlertRule rule)
    {
        return CacheConstants.ALERT_COUNT_KEY + reportedData.getIotId() + ":" + reportedData.getFunctionId() + ":" + rule.getId();
    }

    /**
     * 构建沉默周期redis key
     *
     * @param reportedData 设备上报数据
     * @param rule 报警规则
     * @return redis key
     */
    private String buildAlertSilentKey(DeviceData reportedData, AlertRule rule)
    {
        return CacheConstants.ALERT_SILENT_KEY + reportedData.getIotId() + ":" + reportedData.getFunctionId() + ":" + rule.getId();
    }
}
