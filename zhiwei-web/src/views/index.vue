<template>
  <div class="app-container overview">
    <!-- 顶部：项目定位 -->
    <div class="hero">
      <div class="hero-main">
        <h1>智维云 · 智慧园区安防与能耗管理平台</h1>
        <p class="subtitle">
          面向产业园区的「安防 + 能耗 + IoT」一体化管理平台，覆盖企业入驻建档、巡检计划执行、安防与能耗告警处置、能效诊断报告生成全流程。
        </p>
        <div class="tags">
          <el-tag v-for="tag in techTags" :key="tag" size="small" effect="plain" class="tag">{{ tag }}</el-tag>
        </div>
      </div>
      <div class="hero-side">
        <div class="side-item">
          <span class="num">4</span>
          <span class="label">业务域</span>
        </div>
        <div class="side-item">
          <span class="num">20+</span>
          <span class="label">管理模块</span>
        </div>
        <div class="side-item">
          <span class="num">2</span>
          <span class="label">AI 能力</span>
        </div>
      </div>
    </div>

    <!-- 中部：核心能力 -->
    <el-row :gutter="16" class="cards">
      <el-col :xs="24" :sm="12" :md="8" v-for="item in capabilities" :key="item.title">
        <el-card shadow="hover" class="card">
          <div class="card-head">
            <span class="card-title">{{ item.title }}</span>
          </div>
          <p class="card-desc">{{ item.desc }}</p>
          <ul class="card-points">
            <li v-for="point in item.points" :key="point">{{ point }}</li>
          </ul>
        </el-card>
      </el-col>
    </el-row>

    <!-- 下部：数据链路 -->
    <el-card shadow="never" class="flow">
      <template #header>
        <span class="flow-title">设备告警数据链路</span>
      </template>
      <div class="flow-row">
        <div class="flow-node" v-for="(node, index) in flow" :key="node.name">
          <div class="node-name">{{ node.name }}</div>
          <div class="node-desc">{{ node.desc }}</div>
          <span v-if="index < flow.length - 1" class="arrow">→</span>
        </div>
      </div>
    </el-card>

    <p class="footnote">
      个人全栈实践项目：后端 Spring Boot 多模块架构，前端 Vue3 + Element Plus，IoT 侧对接华为云 IoTDA，AI 侧接入大模型与 Dify 智能体。
    </p>
  </div>
</template>

<script setup>
const techTags = [
  'Spring Boot 3.5',
  'Spring AI',
  'MyBatis-Plus',
  'Redis',
  'Vue 3',
  'Element Plus',
  'Dify',
  '华为云 IoTDA'
];

const capabilities = [
  {
    title: '巡检业务全流程',
    desc: '从企业入驻到巡检执行，串联园区日常运营的关键环节。',
    points: [
      '楼栋 / 房间 / 点位可视化管理',
      '入驻申请、合同签署与费用配置',
      '服务等级、巡检计划与任务排班'
    ]
  },
  {
    title: '设备接入与告警引擎',
    desc: '设备数据实时上报，按规则过滤后精准推送至责任人。',
    points: [
      '华为云 IoTDA AMQP 订阅设备上报',
      '生效时段 / 阈值 / 沉默周期 / 持续周期四层校验',
      '按角色自动分发至巡检员与管理员'
    ]
  },
  {
    title: 'AI 能效能力评估',
    desc: '结合能效基准与大模型，输出可解释的能效诊断报告。',
    points: [
      '两阶段 Prompt：能效分级 + 能耗账单解读',
      'PDF 能耗账单解析与结构化输出',
      '依据评分给出入驻建议与服务等级'
    ]
  }
];

const flow = [
  { name: '智能终端', desc: '电表、水表、烟感、门禁' },
  { name: 'IoTDA', desc: 'AMQP 订阅上报' },
  { name: 'Redis', desc: '实时数据缓存' },
  { name: '告警引擎', desc: '规则匹配与去重' },
  { name: '责任人', desc: '按角色分发' }
];
</script>

<style lang="scss" scoped>
.overview {
  .hero {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 24px;
    padding: 28px 32px;
    background: linear-gradient(120deg, #eef4ff 0%, #f7fbff 60%, #ffffff 100%);
    border: 1px solid #e3ecfb;
    border-radius: 10px;

    h1 {
      margin: 0 0 10px;
      font-size: 24px;
      font-weight: 600;
      color: #1f2d3d;
      letter-spacing: 1px;
    }

    .subtitle {
      margin: 0 0 16px;
      max-width: 720px;
      font-size: 14px;
      line-height: 1.8;
      color: #5c6b7f;
    }

    .tags {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;

      .tag {
        border-color: #cfe1ff;
        color: #2f6bd8;
        background: #f2f7ff;
      }
    }
  }

  .hero-side {
    display: flex;
    gap: 12px;
    flex-shrink: 0;

    .side-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      width: 88px;
      height: 88px;
      background: #fff;
      border: 1px solid #e3ecfb;
      border-radius: 8px;

      .num {
        font-size: 22px;
        font-weight: 600;
        color: #2f6bd8;
      }

      .label {
        margin-top: 4px;
        font-size: 12px;
        color: #8395aa;
      }
    }
  }

  .cards {
    margin-top: 16px;

    .card {
      height: 100%;
      border-radius: 10px;

      .card-title {
        font-size: 16px;
        font-weight: 600;
        color: #1f2d3d;
      }

      .card-desc {
        margin: 10px 0 12px;
        font-size: 13px;
        line-height: 1.7;
        color: #6b7c91;
      }

      .card-points {
        margin: 0;
        padding-left: 18px;

        li {
          font-size: 13px;
          line-height: 2;
          color: #40566f;
        }
      }
    }
  }

  .flow {
    margin-top: 16px;
    border-radius: 10px;

    .flow-title {
      font-size: 15px;
      font-weight: 600;
      color: #1f2d3d;
    }

    .flow-row {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
    }

    .flow-node {
      position: relative;
      flex: 1 1 160px;
      padding: 14px 16px;
      background: #f7faff;
      border: 1px solid #e5eefb;
      border-radius: 8px;

      .node-name {
        font-size: 14px;
        font-weight: 600;
        color: #2f6bd8;
      }

      .node-desc {
        margin-top: 6px;
        font-size: 12px;
        color: #7b8ba1;
      }

      .arrow {
        position: absolute;
        right: -12px;
        top: 50%;
        transform: translateY(-50%);
        color: #b9cbe6;
        font-size: 14px;
      }
    }
  }

  .footnote {
    margin: 16px 0 0;
    font-size: 12px;
    line-height: 1.7;
    color: #93a2b5;
  }
}
</style>
