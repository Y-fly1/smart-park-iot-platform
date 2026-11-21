<template>
  <!-- 疾病诊断 -->
  <el-form
    ref="healthFormRef"
    :model="energyAssessment"
    label-width="180px"
    :rules="healthFormRules"
  >
    <h3 class="title">疾病诊断</h3>
    
    <div class="formMain">
      <!-- 疾病诊断 -->
      <el-form-item label="淘汰设备" prop="diseaseDiagnosis.obsoleteEquipment">
        <el-radio-group v-model="energyAssessment.diseaseDiagnosis.obsoleteEquipment">
          <el-radio label="无">无</el-radio>
          <el-radio label="轻度">轻度</el-radio>
          <el-radio label="中度">中度</el-radio>
          <el-radio label="重度">重度</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="私拉乱接" prop="diseaseDiagnosis.illegalWiring">
        <el-radio-group
          v-model="energyAssessment.diseaseDiagnosis.illegalWiring"
        >
          <el-radio label="无">无</el-radio>
          <el-radio label="精神分裂症">精神分裂症</el-radio>
          <el-radio label="双相情感障碍">双相情感障碍</el-radio>
          <el-radio label="偏执性精神障碍">偏执性精神障碍</el-radio>
          <el-radio label="分裂情感障碍">分裂情感障碍</el-radio>
          <el-radio label="癫痫所致精神障碍">癫痫所致精神障碍</el-radio>
          <el-radio label="精神发育迟滞伴发精神障碍"
            >精神发育迟滞伴发精神障碍</el-radio
          >
        </el-radio-group>
      </el-form-item>

      <el-form-item label="超容用电" prop="diseaseDiagnosis.overCapacity">
        <el-input
          v-model="energyAssessment.diseaseDiagnosis.overCapacity"
          type="textarea"
          :autosize="{ minRows: 4, maxRows: 6 }"
          placeholder="请输入"
          maxlength="100"
          show-word-limit
        />
      </el-form-item>
    </div>

    <h3 class="title">近30天内意外事件</h3>
    <div class="formMain">
      <!-- 近30天内意外事件 -->
      <el-form-item label="超限告警" prop="recent30Days.overLimit">
        <el-radio-group v-model="energyAssessment.recent30Days.overLimit">
          <el-radio
            v-for="option in ptionsData"
            :key="option.value"
            :label="option.value"
          >
            {{ option.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="夜间异常" prop="recent30Days.nightAbnormal">
        <el-radio-group v-model="energyAssessment.recent30Days.nightAbnormal">
          <el-radio
            v-for="option in ptionsData"
            :key="option.value"
            :label="option.value"
          >
            {{ option.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="设备故障" prop="recent30Days.equipmentFailure">
        <el-radio-group v-model="energyAssessment.recent30Days.equipmentFailure">
          <el-radio
            v-for="option in ptionsData"
            :key="option.value"
            :label="option.value"
          >
            {{ option.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="违规用能" prop="recent30Days.violation">
        <el-radio-group v-model="energyAssessment.recent30Days.violation">
          <el-radio
            v-for="option in ptionsData"
            :key="option.value"
            :label="option.value"
          >
            {{ option.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="停电停水" prop="recent30Days.outage">
        <el-radio-group v-model="energyAssessment.recent30Days.outage">
          <el-radio
            v-for="option in ptionsData"
            :key="option.value"
            :label="option.value"
          >
            {{ option.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
    </div>

    <h3 class="title">近30天能耗账单</h3>
    <!-- 近30天能耗账单 -->
    <div class="formMain">
      <el-form-item
        label="能耗账单"
        required
        prop="recent30Days.energyBill"
        
      >
        <FileUpload
          v-model="energyAssessment.recent30Days.energyBill"
          :fileName="baseData.tenantName+'的能耗账单.'+ getFileType(energyAssessment.recent30Days.energyBill)"
          :tenantName="baseData.tenantName"
          :limit="1"
          :fileSize="10"
          :fileType="['pdf']"

        ></FileUpload>
      </el-form-item>
    </div>
  </el-form>
</template>

<script setup>
import { reactive, watch, ref, nextTick, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import {getFileType} from '@/utils';
// 定义 props 接收父组件传递的数据
const props = defineProps({
  formData: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  baseData: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  medications: {
    type: Array,
    default: () => [],
  },
});

const healthFormRef = ref(null);
// 初始化数据结构
const initEnergyAssessment = () => {
  return {
    tenantName: props.baseData.tenantName,
    diseaseDiagnosis: {
      obsoleteEquipment: props.formData.diseaseDiagnosis?.obsoleteEquipment || '',
      illegalWiring: props.formData.diseaseDiagnosis?.illegalWiring || '',
      overCapacity: props.formData.diseaseDiagnosis?.overCapacity || '',
    },
    recent30Days: {
      overLimit: props.formData.recent30Days?.overLimit,
      nightAbnormal: props.formData.recent30Days?.nightAbnormal,
      equipmentFailure: props.formData.recent30Days?.equipmentFailure,
      violation: props.formData.recent30Days?.violation,
      outage: props.formData.recent30Days?.outage,
      energyBill: props.formData.recent30Days?.energyBill || '',
      // energyBillName:props.baseData.tenantName+'的能耗账单.'+getFileType(props.formData.recent30Days?.energyBill)
    },
  };
};

// 能效评估表单校验规则
const healthFormRules = {
  'diseaseDiagnosis.obsoleteEquipment': [
    { required: true, message: '淘汰设备情况为空，请选择淘汰设备情况', trigger: 'change' },
  ],
  'diseaseDiagnosis.illegalWiring': [
    { required: true, message: '请选择私拉乱接情况', trigger: 'change' },
  ],
  'diseaseDiagnosis.overCapacity': [
    { required: true, message: '超容用电为空，请输入超容用电', trigger: 'blur' },
  ],
  'recent30Days.overLimit': [
    { required: true, message: '请选择超限告警发生的次数', trigger: 'change' },
  ],
  'recent30Days.nightAbnormal': [
    { required: true, message: '请选择夜间异常发生的次数', trigger: 'change' },
  ],
  'recent30Days.equipmentFailure': [
    { required: true, message: '请选择设备故障发生的次数', trigger: 'change' },
  ],
  'recent30Days.violation': [
    { required: true, message: '请选择违规用能发生的次数', trigger: 'change' },
  ],
  'recent30Days.outage': [
    { required: true, message: '请选择停电停水发生的次数', trigger: 'change' },
  ],
  'recent30Days.energyBill': [
    { required: true, message: '能耗账单为空，请上传能耗账单', trigger: 'change' },
  ],
};

// 用药数据
const medications = ref([
  {
    name: '',
    method: '',
    dose: '',
    frequency: '',
  },
]);

// 定义 emit 向父组件传递事件
const emit = defineEmits([
  'update:formData',
  'update:medications',
  'file-change',
]);

const ptionsData = [
  { label: '无', value: 0 },
  { label: '发生过1次', value: 1 },
  { label: '发生过2次', value: 2 },
  { label: '发生过3次以上', value: 3 },
];

// 使用本地响应式数据副本
const energyAssessment = reactive(initEnergyAssessment());

// 用药数据
const medicationsData = reactive([
  {
    name: '',
    method: '',
    dose: '',
    frequency: '',
  },
]);

// 文件列表
const fileList = ref([]);

// 监听props变化，同步到本地数据
watch(
  () => props.formData,
  (newValue) => {
    console.log('父组件数据变化:', newValue);
    // 更新疾病诊断数据
    if (newValue.diseaseDiagnosis) {
      energyAssessment.diseaseDiagnosis = {
        ...energyAssessment.diseaseDiagnosis,
        ...newValue.diseaseDiagnosis,
      };
    }

    // 更新30天事件数据
    if (newValue.recent30Days) {
      energyAssessment.recent30Days = {
        ...energyAssessment.recent30Days,
        ...newValue.recent30Days,
      };
    }
  },
  { deep: true },
);

// 监听本地数据变化，更新到父组件
watch(
  energyAssessment,
  (newValue) => {
    nextTick(() => {
      console.log(healthFormRef.value && newValue.recent30Days.energyBill); 
      if (healthFormRef.value && newValue.recent30Days.energyBill) {
        healthFormRef.value.validateField('recent30Days.energyBill');
      }
    });
    emit('update:formData', { ...newValue });
  },
  { deep: true },
);

// 添加用药
const addMedication = (index) => {
  medicationsData.splice(index + 1, 0, {
    name: '',
    method: '',
    dose: '',
    frequency: '',
  });
};

// 删除用药
const removeMedication = (index) => {
  medicationsData.splice(index, 1);
};

// 处理文件变化
const handleFileChange = (file) => {
  if (file.raw && file.raw.type === 'application/pdf') {
    energyAssessment.recent30Days.energyBill = file.raw;
    fileList.value = [file];
    nextTick(() => {
      if (healthFormRef.value) {
        healthFormRef.value.validateField(
          'recent30Days.energyBill',
          (error) => {
            console.log('energyBill 验证结果:', error ? '失败' : '成功');
          },
        );
      }
    });
  } else {
    ElMessage.error('请上传PDF文件');
  }
};

// 文件上传前校验
const beforeUpload = (file) => {
  const isPdf = file.type === 'application/pdf';
  const isLt60M = file.size / 1024 / 1024 < 10;

  if (!isPdf) {
    ElMessage.error('上传文件必须是PDF格式');
    return false;
  }

  if (!isLt60M) {
    ElMessage.error('上传文件大小不能超过10MB');
    return false;
  }

  return true;
};

// 处理文件删除
const handleRemove = () => {
  energyAssessment.recent30Days.energyBill = null;
  fileList.value = [];
};

// 表单验证方法
const validate = () => {
  return new Promise((resolve, reject) => {
    if (!healthFormRef.value) {
      resolve(true);
      return;
    }

    healthFormRef.value.validate((valid) => {
      if (valid) {
        resolve(true);
      } else {
        reject(new Error('表单校验失败'));
      }
    });
  });
};

// 重置表单
const resetFields = () => {
  if (healthFormRef.value) {
    healthFormRef.value.resetFields();
    Object.assign(energyAssessment, initEnergyAssessment());
  }
};

// 页面加载时初始化
onMounted(() => {
  console.log('energyAssessment 组件挂载，初始数据:', energyAssessment);
});

defineExpose({
  healthFormRef,
  energyAssessment,
  validate,
  resetFields,
});
</script>

<style scoped>
.title {
  margin: 20px 0 10px 0;
  color: #333;
  font-size: 16px;
  font-weight: bold;
}

.formMain {
  padding: 0 20px;
}

.basicinUpload {
  margin-top: 10px;
}

:deep(.el-radio-group) {
  display: flex;
  flex-wrap: wrap;
}

:deep(.el-radio) {
  margin-right: 20px;
  margin-bottom: 10px;
}

:deep(.el-textarea) {
  width: 100%;
}
</style>
