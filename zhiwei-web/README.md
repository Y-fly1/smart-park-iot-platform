# 智维云 · 智慧园区安防与能耗管理平台（前端）

园区安防与能耗管理后台的前端工程，对应后端仓库（Spring Boot 多模块）。

Vue 3 + Vite + Element Plus + Pinia + ECharts，覆盖企业档案、楼栋与点位可视化、入驻办理、巡检计划与任务、设备与告警、能效评估报告、Dify 智能问答等业务页面。

---

## 一、页面结构

| 目录 | 页面 |
|---|---|
| `views/park/tenants` | 入驻企业档案（列表、详情、资质与联系人） |
| `views/park/building` | 楼栋 → 房间 → 点位维护 |
| `views/park/pointMonitor` | 点位监控看板（占用状态、绑定设备、实时数据） |
| `views/park/settleIn` | 入驻办理（企业信息 → 计费配置 → 联系人 → 合同签署） |
| `views/park/contract` | 入驻合同管理 |
| `views/park/serviceLevel` | 服务等级与费用 |
| `views/park/inspectionPlan` / `inspectionItem` | 巡检计划编排与巡检项维护 |
| `views/park/device` / `alertData` / `alertRule` | 设备台账、告警记录、告警规则 |
| `views/park/energyAssessment` | 能效评估录入、分步向导、诊断报告与雷达图 |
| `views/park/knowledgeBase` / `dify` / `zhixun` | 知识库、Dify 智能体对话、运维问答 |
| `views/park/reservation` | 访客预约 |

---

## 二、技术要点

### 1. 动态路由与按钮级权限

登录后由后端返回菜单树，前端按 `component` 字段动态注册路由（`router/permission.js`），按钮级权限用 `v-hasPermi` 指令控制，权限标识统一为 `park:*:*`。

### 2. SSE 流式对话

`views/park/dify` 与 `zhixun` 采用 `fetch` + `ReadableStream` 读取流式响应，逐块解析并增量渲染 Markdown（`marked` + `highlight.js`），避免整段重排；支持中断请求与会话切换。

### 3. 多步表单

入驻办理拆成四个步骤组件，父组件维护表单数据与校验状态，子组件各自独立校验，最后合并提交；巡检计划用 `vuedraggable` 编排巡检项顺序。

### 4. 空间可视化

按"楼栋 → 房间 → 点位"三层数据渲染看板，用颜色区分点位状态（正常 / 告警 / 停用 / 离线），点击点位查看绑定设备与最近上报数据，支持在看板内新增房间与点位。

### 5. 能效评估报告

评估表单按四个维度（用电、用水、燃气、暖通）收集分级与运行风险，提交后由后端两阶段调用大模型生成报告；前端用 ECharts 呈现八项用能得分雷达图与分布图，报告支持 Markdown 渲染与 PDF 导出。

---

## 三、启动

```bash
npm install
npm run dev          # 开发环境，默认代理到 http://localhost:8080
npm run build:prod   # 生产构建
```

接口地址在 `.env.development` / `.env.production` 中配置：

```
VITE_APP_BASE_API = '/dev-api'
VITE_APP_TITLE = '智维云 · 智慧园区安防与能耗管理平台'
```

开发环境通过 `vite.config.js` 的 `server.proxy` 转发到后端，无需处理跨域。

---

## 四、目录结构

```
src/
├── api/park/            业务接口封装
├── views/park/          业务页面
├── components/          通用组件
├── layout/              布局与侧边栏
├── router/              静态路由与权限路由
├── store/               Pinia 状态
├── directive/           v-hasPermi 等指令
└── utils/               request 封装、权限、格式化
```

---

## 五、说明

- 基于 RuoYi-Vue3 前端脚手架（MIT License）二次开发，保留原始版权声明；
- 演示数据为虚构，仅供功能展示。
