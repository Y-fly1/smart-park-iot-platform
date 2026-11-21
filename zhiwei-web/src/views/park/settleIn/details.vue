<template>
  <div class="app-container">
    <div v-if="checkId === ''" class="dialog-form cleckInForm">
      <el-form
        ref="formRef"
        :model="formData"
        size="default"
        label-width="120px"
      >
        <!-- 基本信息 -->
        <ApplyForm
          ref="applyForm"
          :type="type"
          :applyFormInfo="applyFormInfo"
          @getFormData="getFormData"
        />

        <!-- 联系人信息 -->
        <Family
          :type="type"
          :familyInfo="familyInfo"
          @setFamily="setFamily"
          ref="familyForm"
        />

        <!-- 入驻配置 -->
        <ConfigurationForm
          :type="type"
          :configInfo="configInfo"
          ref="configForm"
          @getFormData="getFormData"
        />

        <!-- 签约办理 -->
        <TransactForm
          ref="transactForm"
          @getFormData="getFormData"
          :type="type"
          :transactFormInfo="transactFormInfo"
        />
      </el-form>
    </div>
    <div class="info" v-else>
      <ApplyBase :baseData="applyFormInfo"></ApplyBase>
      <baseFamily :baseData="familyInfo"></baseFamily>
      <ApplyConfigBase :baseData="configInfo"></ApplyConfigBase>
      <SignBaseInfo
        :baseData="transactFormInfo"
        :elderVo="applyFormInfo"
      ></SignBaseInfo>
    </div>
    <!-- 提交按钮 -->
    <div class="info center">
      <el-button @click="goBack">返回</el-button>
      <el-button v-if="checkId === ''" type="primary" @click="submitForm"
        >提交</el-button
      >
      <el-button type="primary" @click="handleBill">
      账单预览
    </el-button>
    </div>
    <!-- 账单预览弹层 -->
  <BillDetails
    :visible="billVisible"
    :form-data="formData"
    :applyFormInfo="applyFormInfo"
    :base-data="configInfo"
    @handleClose="handleClose"
  ></BillDetails>
  <!-- end -->
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter, useRoute } from 'vue-router';

//引入组件
import ApplyForm from './components/ApplyForm.vue';
import Family from './components/Family.vue';
import ConfigurationForm from './components/ConfigurationForm.vue';
import TransactForm from './components/TransactForm.vue';
// 查看详情
import ApplyBase from './components/ApplyBase.vue';
// 配置详情
import ApplyConfigBase from './components/ApplyConfigBase.vue';
// 联系人信息
import baseFamily from './components/baseFamily.vue';
// 合同详情
import SignBaseInfo from './components/SignBaseInfo.vue';
// 账单预览
import BillDetails from './components/BillDetails.vue'
import { applySettleIn, getSettleInInfo } from '@/api/park/settleIn';

const router = useRouter();
const route = useRoute();

//表单数据
const formData = ref({});

//表单对象 - 父组件的表单引用（主要用于包裹）
const formRef = ref(null);

//子组件引用

const applyForm = ref(null);
const familyForm = ref(null);
const configForm = ref(null);
const transactForm = ref(null);

//联系人信息列表
const familyUserData = ref([]);

const type = ref('');
const applyFormInfo = ref({});
const familyInfo = ref([]);
const configInfo = ref({});
const transactFormInfo = ref({});
const checkId = ref('');
const billVisible = ref(false) // 账单预览弹层
onMounted(() => {
  type.value = route.query.type;
  checkId.value = route.query.id ? route.query.id : '';
  console.log(checkId.value);
  if (type.value === 'read' && route.query.id) {
    getSettleIn(route.query.id);
  }
});

//查看入驻详情
const getSettleIn = (id) => {
  getSettleInInfo(id).then((res) => {
    applyFormInfo.value = res.data.settleInTenantVo || {};
    familyInfo.value = res.data.elderFamilyVoList || [];
    configInfo.value = res.data.settleInConfigVo || {};
    transactFormInfo.value = res.data.contract || {};
  });
};

//提交表单
const submitForm = async () => {
  try {
    // 依次校验所有子组件
    const applyValid = await applyForm.value?.validate();
    const configValid = await configForm.value?.validate();
    const transactValid = await transactForm.value?.validate();

    // 先检查联系人信息是否完整
    if (!familyForm.value?.validate()) {
      // ElMessage.error('联系人信息不完整，请输入联系人信息');
      return;
    }
    if (!applyValid || !configValid || !transactValid) {
      return;
    }
    //组装请求参数
    const params = {
      energyAssessmentId: formData.value.energyAssessmentId, //能效评估ID
      //基本信息
      settleInTenantDto: applyForm.value?.getFormData() || {},
      //联系人信息
      elderFamilyDtoList: familyUserData.value,
      //入驻配置
      settleInConfigDto: configForm.value?.getFormData() || {},
      //签约办理
      settleInContractDto: transactForm.value?.getFormData() || {},
    };

    console.log('提交参数:', params);

    //发起请求-申请入驻
    const res = await applySettleIn(params);
    if (res.code == 200) {
      ElMessage.success('入驻成功');
      router.push({ path: '/enterQuit/settleIn' }); //跳转到列表查询
    } else {
      ElMessage.error(res.msg || '提交失败');
    }
  } catch (error) {
    console.error('提交失败:', error);
    // ElMessage.error('提交失败，请重试');
  }
};

//获取子组件的表单数据
const getFormData = (val) => {
  //合并数据到formData
  formData.value = { ...formData.value, ...val };
  transactFormInfo.value.name = formData.value.name;
  console.log('表单数据更新:', formData.value);
};

//接收联系人信息
const setFamily = (val) => {
  familyUserData.value = val;
};
// 返回列表页enterQuit/settleIn

const goBack = () => {
  router.push({
    path: '/enterQuit/settleIn',
  });
};

// 账单预览
const handleBill = () => {
  billVisible.value = true
}
// 关闭账单预览
const handleClose = () => {
  billVisible.value = false
}
</script>
