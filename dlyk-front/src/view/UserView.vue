<template>
  <el-button type="primary">添加用户</el-button>
  <el-button type="primary">批量删除</el-button>

  <el-table
      :data="userList"
      style="width: 100%"
      @selection-change="handleSelectionChange"
  >
    <el-table-column type="selection" width="55"/>
    <el-table-column type="index" label="序号" width="120"/>
    <el-table-column property="name" label="账号" width="120"/>
    <el-table-column property="address" label="姓名" show-overflow-tooltip/>
    <el-table-column property="address" label="联系方式" show-overflow-tooltip/>
    <el-table-column property="address" label="邮箱" show-overflow-tooltip/>
    <el-table-column property="address" label="创建时间" show-overflow-tooltip/>

    <el-table-column label="操作" show-overflow-tooltip>

    </el-table-column>
  </el-table>
</template>

<script>
import {defineComponent} from "vue";

export default defineComponent({
  name: "UserView",
  data() {
    return {
      //用户列表数据
      userList: [{}]
    }
  },

  methods: {
    //勾选或取消勾选时触发该函数
    handleSelectionChange() {

    }
  }
})

</script>


<style scoped>

</style>
