# 智维云 · 智慧园区安防与能耗管理平台（后端）

面向产业园区的安防与能耗一体化管理后台：企业入驻建档、楼栋/房间/点位管理、安防与能耗设备接入、告警规则引擎、巡检计划与任务、能效评估报告，以及基于大模型与知识库的运维问答。

前端仓库：`zhiwei-ui`（Vue 3 + Element Plus）

---

## 一、业务定位

园区运营方在同一个后台里管三件事：

| 业务域 | 具体能力 |
|---|---|
| **资产与入驻** | 楼栋 → 房间（功能区）→ 监测点位三级台账；企业入驻申请、计费配置、合同签署与退租 |
| **安防与能耗监控** | 智能终端（电表、水表、烟感、门禁、摄像头）数据接入；阈值/时段/持续周期多维告警；点位与设备实时状态 |
| **巡检与能效** | 服务等级与巡检计划、巡检任务派发与执行留痕；四维能效评估 + 能耗账单解读，输出诊断报告 |

```
企业入驻 ──► 点位绑定 ──► 终端上报 ──► 规则引擎 ──► 告警工单 ──► 巡检闭环
                                │
                                └──► 能效评估（四维分级 + 账单解读）──► 诊断报告
```

---

## 二、技术栈

| 层次 | 选型 |
|---|---|
| 基础框架 | Spring Boot 3.5、Spring Security + JWT、MyBatis |
| 模块架构 | Maven 多模块（10 个模块） |
| 数据存储 | MySQL 8、Redis（业务缓存 + 告警状态 + 向量索引）、阿里云 OSS |
| IoT 接入 | 华为云 IoTDA，AMQP 队列订阅设备上报 |
| AI 能力 | Spring AI（ChatClient）、通义千问（OpenAI 兼容协议）、Dify 智能体 API、Redis Vector Store |
| 文档与监控 | SpringDoc OpenAPI、Druid 监控、操作日志与登录日志 |

**规模**：Java 源码 455 个文件 / 50 个 Controller / 143 个 REST 接口 / 44 张表。

---

## 三、模块说明

| 模块 | 职责 |
|---|---|
| `zhiwei-admin` | 启动模块、Web 层配置、全局异常与安全入口 |
| `zhiwei-framework` | Spring Security、JWT、拦截器、数据源与 Redis 配置 |
| `zhiwei-system` | 用户、角色、菜单、部门、字典、日志等平台基础能力 |
| `zhiwei-common` | 通用工具、统一响应、分页、Excel 导入导出、注解 |
| `zhiwei-park-platform` | **核心业务**：楼栋/房间/点位、企业入驻、合同、巡检、告警、能效评估 |
| `zhiwei-ai` | 大模型接入、Dify 流式对话、会话记忆、知识库检索、AI 工具 |
| `zhiwei-oss` | 对象存储上传下载 |
| `zhiwei-quartz` | 定时任务（巡检任务生成、告警超时处理） |
| `zhiwei-generator` | 代码生成器 |

---

## 四、核心实现

### 1. 告警规则引擎（四层校验 + Redis 状态）

设备上报数据不直接产生告警，而是经过四层过滤，避免"告警风暴"：

1. **生效时段**：规则只在配置的时间窗内生效（如仅夜间 22:00–06:00 监测异常用电）；
2. **阈值比较**：支持 `> >= < <= = !=` 六种运算符，按功能点（functionId）取值比较；
3. **持续周期**：要求连续 N 个上报周期均越限才触发，过滤单点抖动；
4. **沉默周期**：同一设备同一规则在沉默期内只告警一次，状态存 Redis 并带 TTL。

触发后按设备类型（移动设备 / 固定设备）解析责任人，落库告警记录并推送。

相关代码：`AlertRuleServiceImpl`、`AlertDataServiceImpl`、`DeviceDataServiceImpl`、`job/AmqpClient`。

### 2. IoT 设备接入

- 通过华为云 IoTDA 的 AMQP 队列订阅设备属性上报，解析后写入 `device_data`，同时刷新 Redis 中的设备最新状态；
- 未配置云侧凭证时自动跳过订阅并打印告警日志，保证本地无云资源也能启动；
- 设备与点位的绑定关系支持"移动设备跟随人员、固定设备绑定房间"两种定位方式。

### 3. 两阶段 AI 能效评估

**阶段一：四维分级 → 能效等级**

按用电、用水、燃气、暖通四个维度采集分级（0–3），叠加近 30 天运行风险（超限告警、设备故障、违规用能、夜间异常、停电停水）与设备状态（淘汰设备、私拉乱接、超容用电、计量缺失），由大模型按两套规则给出**初步等级**与**最终等级**及调整理由，返回严格 JSON。

**阶段二：能耗账单 → 诊断报告**

用户上传的能耗账单 PDF 先由 `PDFUtil` 抽取文本，再送入第二套 Prompt，输出：能效指数、风险等级、异常项清单（含实测值/参考值/解读/整改建议）、八项用能得分（照明、暖通、动力、插座、电梯、给排水、充电桩、其他）与总体结论。

两阶段结果分别落 `energy_assessment_report`，前端用 ECharts 呈现雷达图与分布图。

> 规则兜底：Prompt 中明确给出分级规则与调整规则，模型只做规则匹配与文本生成，避免"自由发挥"；返回结果统一用 Hutool `JSONUtil` 反序列化，异常时降级处理而不是抛错中断。

### 4. Dify 智能体与知识库问答

- `DifyServiceImpl` 以 `HttpClient` 调 Dify 的 `/chat-messages`，手写 SSE 逐行解析（`data:` 前缀、事件分片），通过 `SseEmitter` 推给前端；
- 会话记忆存 Redis（`RedisChatMemory`），支持按 conversationId 续聊与历史回溯；
- 知识库采用 Redis Vector Store + 通义 embedding，文档切片入索引，问答时先检索再交给模型，降低幻觉。

---

## 五、快速启动

### 1. 环境要求

JDK 17、Maven 3.8+、MySQL 8、Redis 6+

### 2. 初始化数据库

```bash
mysql -uroot -p -e "CREATE DATABASE zhiwei DEFAULT CHARSET utf8mb4;"
mysql -uroot -p zhiwei < sql/zhiwei.sql
```

### 3. 配置环境变量

复制 `.env.example` 为 `.env` 并按需填写；未填写的项走 `application-dev.yml` 中的本地默认值。

```bash
DB_HOST=localhost
DB_PORT=3306
DB_NAME=zhiwei
DB_USERNAME=root
DB_PASSWORD=your_password
REDIS_HOST=localhost
REDIS_PORT=6379
JWT_SECRET=please-change-this-secret
```

AI 与云侧能力均为**可选项**：不配置 `BAI_LIAN_API_KEY`、`DIFY_API_KEY`、华为云 AK/SK 时，服务仍可正常启动，仅对应功能不可用。

### 4. 启动

```bash
mvn clean package -DskipTests
java -jar zhiwei-admin/target/zhiwei-admin.jar
```

默认端口 `8080`，接口文档 `http://localhost:8080/swagger-ui/index.html`。

---

## 六、目录结构

```
zhiwei/
├── zhiwei-admin/                 启动模块
├── zhiwei-common/                通用能力
├── zhiwei-framework/             安全与框架配置
├── zhiwei-system/                平台基础（用户/角色/菜单）
├── zhiwei-park-platform/         园区业务
│   └── src/main/java/com/zhiwei/park/
│       ├── controller/           接口层
│       ├── service/              业务层
│       ├── mapper/               数据访问
│       ├── domain/               实体 / DTO / VO
│       ├── job/                  IoT 订阅与定时任务
│       └── constants/            提示词与常量
├── zhiwei-ai/                    大模型与 Dify
├── zhiwei-oss/                   对象存储
├── zhiwei-quartz/                定时任务
├── zhiwei-generator/             代码生成
└── sql/                          建表与演示数据
```

---

## 七、边界说明

- 本项目为个人全栈实践项目，非生产系统：未做压测、未接入真实支付与短信、未实现多租户隔离；
- IoT 部分依赖华为云 IoTDA，本地演示可用模拟上报接口；
- 演示数据中的企业与设备信息均为虚构。

## 八、开源协议

基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue)（MIT License）二次开发，保留原始版权声明；业务模块（`zhiwei-park-platform`、`zhiwei-ai`）为本人实现。
