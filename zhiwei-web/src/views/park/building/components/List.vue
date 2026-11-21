<!--区域类型列表-->
<template>
  <div v-if="floorData.length">
    <!-- 房间列表 -->
    <div v-if="roomVoList && roomVoList.length" class="houseList">
      <div
        v-for="(roomItem, roomindex) in roomVoList"
        :key="roomindex"
        class="item"
      >
        <!-- 房间标题 -->
        <div class="title">
          <div class="lText">
            <span class="name">{{ roomItem.code }}</span
            ><span class="num">{{ roomItem.typeName }}</span>
            <div class="icon">
              <span class="edit" @click="handleRoomEdit(roomItem.id)"></span
              ><span
                class="delect"
                :class="
                  roomItem.bedVoList && roomItem.bedVoList.length > 0
                    ? 'forbidDelect'
                    : ''
                "
                @click="handleDelete(2, roomItem)"
              ></span>
              <!-- <span class="check" @click="handleRoomCheck(roomItem.id)"></span> -->
            </div>
          </div>
          <span class="add" @click="handlePointAdd(roomItem.id)"></span>
        </div>
        <!--  -->
        <!-- 点位列表 -->
        <div class="bedList">
          <div v-if="roomItem.bedVoList && roomItem.bedVoList.length">
            <ul>
              <li
                v-for="(item, index) in roomItem.bedVoList"
                :key="index"
                class="leisure"
                :class="
                      item.pointStatus === 0?'leisure':item.pointStatus === 1?'settleIn':'goOutIcon'
                    "
              >
              
                <div class="bedIcon">
                  <span
                    :class="item.ename ? 'settleInIcon' : 'leisureIcon'"
                  ></span>
                </div>
                <div class="bedText">
                  <p>点位编号：{{ item.pointCode }}</p>
                  <p
                    v-if="
                      item.pointStatus === 2 ||
                      (item.pointStatus === 1 && item.ename)
                    "
                  >
                    {{ item.ename ? item.ename : '空闲' }}
                  </p>
                  <p v-else>空闲</p>
                </div>
                <div class="bedHover" :class="routeType !== 'config' ? '' : 'bedNoHover'">
                  <span
                    class="edit"
                    :class="item.pointStatus!==0? 'forbidDelect' : ''"
                    @click="handlePointEdit(item)"
                  ></span>
                  <!-- -->
                  <span
                    class="delect"
                    :class="item.pointStatus!==0? 'forbidDelect' : ''"
                    @click="handleDelete(3, item)"
                  ></span>
                </div>
              </li>
            </ul>
          </div>
          <div v-else class="noData">当前房间没有安排点位</div>
        </div>
        <!-- end -->
      </div>
    </div>
    <!-- end -->
    <div v-else><NoData></NoData></div>
  </div>
  <div v-else><NoData></NoData></div>
</template>
<script setup>
import {ref} from 'vue'
// 点位信息新增房间
import NoData from '@/components/noData/index.vue' // 无数据提示组件
// ------定义变量------

// 获取父组件值、方法
const props = defineProps({
  //  房间数据
  roomVoList: {
    type: Array
  },
  // 楼栋数据
  floorData: {
    type: Array
  },
  bedSelectData: {
    type: Object,
    default: () => {
      return {}
    }
  }
})
// 触发父级事件
const emit = defineEmits([
  'handleEdit',
  'handleDelete',
  'handleAdd',
  'handlePointEdit',
  'handleCheck',
  'getPointInfo'
])
const isCheck = ref('')
const isRoom = ref('')
// ------定义方法------
// 删除
const handleDelete = (type, item) => {
  // 如果房间里有点位或者点位上有住人，禁止删除此信息
  if ((item.bedVoList && item.bedVoList.length === 0) || (type===3&&!item.pointStatus)) {
    emit('handleDelete', type, item)
  }
}
// 房间编辑
const handleRoomEdit = (id) => {
  emit('handleEdit', id)
}
// 房间查看
const handleRoomCheck = (id) => {
  emit('handleCheck', id)
}
// 点位添加
const handlePointAdd = (id) => {
  emit('handleAdd', id)
}
// 点位编辑
const handlePointEdit = (item) => {
  if (!item.pointStatus) {
    emit('handlePointEdit', item.id)
  }
}
// 点位选择
const handleRadio = (room, item) => {
  isCheck.value = item.id
  isRoom.value = item.roomId
  const obj = {
    ...room,
    ...item,
    roomId: room.id,
    pointId: item.id
  }
  emit('getPointInfo', obj)
}
</script>
