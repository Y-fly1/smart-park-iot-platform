<!--添加企业选择企业弹层-->
<template>
  <div class="dialogBox tableDialog oldSelectDialog">
    <el-dialog
      title="选择企业"
      v-model="visible"
      class="elDialogBox"
      @close="handleClose"
      :close-on-click-modal="false"
      :before-close="handleBeforeClose"
    >
      <div class="dialogCenter">
        <!-- 搜索 -->
        <el-form
          :model="searchData"
          ref="ruleForm"
          :inline="true"
          label-width="100px"
        >
          <el-form-item label="企业名称" prop="tenantName">
            <el-input
              v-model="searchData.tenantName"
              placeholder="请输入"
              clearable
              class="elInputW"
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="企业统一社会信用代码" prop="creditCode">
            <el-input
              v-model="searchData.creditCode"
              placeholder="请输入"
              clearable
              @keyup.enter="handleSearch"
              class="elInputW"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleSearch"
              >搜索</el-button
            >
            <el-button icon="Refresh" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>

        <!-- end -->
        <div class="oldTable">
          <!-- <div class="tit">企业信息</div> -->
          <div class="table">
            <el-table
          v-loading="props.dataLoading"
          row-key="id"
          :data="dialogData"
          @row-click="chooseone"
          @current-change="handleCurrentChange"
        >
          <el-table-column width="55" label="选项">
            <template #default="scope">
              <el-radio v-model="selectedRowKeys" :label="scope.row.id"
                ><span class="el-radio__label"></span
              ></el-radio>
            </template>
          </el-table-column>
          <el-table-column label="序号" type="index" width="50" />
          <el-table-column label="企业名称" align="center" prop="tenantName" />
          <el-table-column label="统一社会信用代码" align="center" prop="creditCode" />
          <el-table-column label="核心建议" align="center" prop="energyScore">
            <template #default="scope">
          <span v-if="scope.row.coreSuggestion === 0"
            ><span class="tagCol2">不建议入驻</span></span
          >
          <span v-else><span class="tagCol1">建议入驻</span></span>
        </template>
          </el-table-column>
          <!-- <el-table-column label="能效评分" align="center" prop="energyScore" />
          <el-table-column
            label="推荐服务等级"
            align="center"
            prop="serviceLevelName"
          /> -->
        </el-table>
        <div class="pagination-wrapper">
        <Pagination
          v-show="pagination.total > 0"
          :total="pagination.total"
          :pageSizes="[5,10, 20, 30, 50]"
          v-model:page="pagination.pageNum"
          v-model:limit="pagination.pageSize"
          @pagination="getList"
        />
        </div>
        <!-- end -->
          </div>
        
        </div>
        
        <!-- 底部按钮 -->
        <div class="dialog-footer">
          <el-button @click="handleClose">取 消</el-button>
          <el-button type="primary" @click="handleSub">确 定</el-button>
          
        </div>
        <!-- end -->
      </div>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import {reactive, ref, watch} from 'vue';
import {ElMessage} from 'element-plus';
// 组件
// 获取父组件值、方法
const props = defineProps({
  // 获取列表数据
  dialogData: {
    type: Array,
    default: () => [],
  },
  // 弹层隐藏显示
  dialogVisible: {
    type: Boolean,
    default: false,
  },
  // 分页
  pagination: {
    type: Object,
    default: () => ({}),
  },
  // 加载状态
  dataLoading: {
    type: Boolean,
    default: false,
  },
  // 基本信息
  formData: {
    type: Object,
    default: () => ({}),
  },
});
// ------定义变量------
const emit = defineEmits([
  'handleCloseDialog',
  'getCurrent',
  'getOldList',
  'handleSelectOld',
  'handleSearch',
  'handleReset',
]); // 子组件获取父组件事件传值
const regTable = ref(); // 定义表格ref
const ruleForm = ref(null); // 定义表单ref
const visible = ref(false); // 用来控制弹层显示隐藏
const selectedRowKeys = ref('');
const checkTabData = reactive<Object | any>({}); // 选中的list
const searchData = ref({});
// 监听弹层是显示还是隐藏
watch([() => props.dialogVisible, () => props.formData], ([newVisible, newFormData]) => {
  visible.value = newVisible;
  if (visible.value) {
    // 优化回显逻辑，避免空值赋值
    if (newFormData.energyAssessmentId) {
      selectedRowKeys.value = newFormData.energyAssessmentId;
      checkTabData.value = newFormData;
    } else {
      selectedRowKeys.value = '';
      checkTabData.value = {};
    }
  } else {
    // 弹层关闭时立即清空选中状态
    selectedRowKeys.value = '';
    checkTabData.value = {};
  }
}, { immediate: true, deep: true });
// ------定义方法------
// 确认提交
const handleSub = async () => {
  // 修正判断条件，从length改为是否为空
  if (!selectedRowKeys.value) {
    ElMessage.error('未选择企业，请选择企业信息');
  } else {
    console.log(checkTabData.value);
    emit('handleSelectOld', checkTabData.value);
    handleClose();
  }
};
// 关闭弹层
const handleClose = () => {
  emit('handleCloseDialog');
  ruleForm.value?.resetFields(); // 可选链避免空值报错
  selectedRowKeys.value = ''; 
  checkTabData.value = {};
  // 清空搜索数据，避免残留搜索条件影响下次打开
  searchData.value = {};
};
const handleBeforeClose = (done) => {
  handleClose(); // 执行关闭逻辑
  done(); // 确认关闭弹层
};
// 触发复选框
const handleChangeSelection = (index, { selectedRowData }) => {
  selectedRowKeys.value = index;
  checkTabData.value = selectedRowData;
};
// 点击翻页
const onPageChange = (pageInfo) => {
  emit('getCurrent', pageInfo);
};
// 搜索
const handleSearch = () => {
  emit('handleSearch',searchData.value);
};
// 清空搜索数据
const handleReset = () => {
  ruleForm.value.resetFields(); // 清除表单
  emit('handleReset');
};
const getList = () => {
  emit('getOldList');
};
// 触发单选框
const handleCurrentChange = (row) => {
  checkTabData.value = row;
};
const chooseone = (row) => {
  selectedRowKeys.value = row.id;
  console.log(selectedRowKeys.value);
};
</script>
