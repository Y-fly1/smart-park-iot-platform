<template>
  <div class="min-h content-layout">
    <div class="bg-wt">
    <!-- 楼栋tab导航 -->
    <SwitchBar
      ref="tabBar"
      :data="tabData"
      :route-type="routeType"
      @change-id="getBuildingId"
    ></SwitchBar>
    <!-- end -->
    <!-- 区域类型列表 -->
    <HouseList
      :key="buildingId"
      :buildingId="buildingId"
      :base-data="roomVoList"
      :building-data="tabData"
      :route-type="routeType"
    ></HouseList>
    <!-- end -->
  </div>
  </div>
</template>
<script setup>
import useUserStore from '@/store/modules/user'
// 接口
import {getRoomPointDeviceList, listAllDeviceBuilding} from '@/api/park/building'
// 组件
// tab切换
import SwitchBar from '@/components/switchBar/SwitchBartop.vue'

// 区域类型列表
import HouseList from './components/List.vue'
// ------定义变量------
const route = useRoute() // 获取局部
const userStore = useUserStore()
// 楼栋变量
const tabBar = ref(null) // 楼栋的ref
const tabData = ref([]) // 定义tab切换数据
const roomVoList = ref([])
const buildingId = ref('') // 楼栋id
const floorIndex = ref(null)
// 房间变量
const routeType = ref(null)
// 获取全部数据
const allData = ref([])
const timerId = ref(null)
onUnmounted(() => {
  clearInterval(timerId.value)
})
// 默认加载数据
onMounted(() => {
  if (route.query.type !== undefined) {
    routeType.value = route.query.type
  }
  getAllBuildingList()
})
// ------定义方法------
// 获取楼栋数据
const getAllBuildingList = async () => {
  const res = await listAllDeviceBuilding()
  allData.value = res.data
  if (res.code === 200) {
    tabData.value = res.data
    // 首次进来需要拿楼栋第一条数据的id来获取当前数据
    if (buildingId.value === '' && tabData.value.length) {
      buildingId.value = tabData.value[0].id
      userStore.buildingId = tabData.value[0].id
    }
    getList()
  }
}
// 根据不同的楼栋id楼栋信息
const getList = async () => {
  const res = await getRoomPointDeviceList(buildingId.value)
  if (res.code === 200) {
    roomVoList.value = res.data
  }
}
// 获取楼栋id
const getBuildingId = (id, i) => {
  // 点击异常楼栋后消除对应的红点异常

  userStore.buildingId = buildingId.value
  floorIndex.value = i
  buildingId.value = id
  getList()
}
</script>
<style lang="less" src="./../building/index.scss"></style>
<style lang="scss" scoped>
.smartPoint {
  min-height: calc(100vh - 130px);
}
</style>
