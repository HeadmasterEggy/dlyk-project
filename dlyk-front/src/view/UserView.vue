<template>
  <el-button plain @click="userDialogVisible = true" type="primary">添加用户</el-button>
  <el-button type="danger">批量删除</el-button>

  <!--新增用户的弹窗(对话框)-->
  <el-dialog v-model="userDialogVisible" :title="userQuery.id > 0 ? '编辑用户' : '新增用户'" width="55%" center
             draggable>

    <el-form ref="userRefForm" :model="userQuery" label-width="110px" :rules="userRules">
      <el-form-item label="账号" prop="loginAct">
        <el-input v-model="userQuery.loginAct"/>
      </el-form-item>

      <el-form-item label="密码" v-if="userQuery.id > 0"><!--编辑-->
        <el-input type="password" v-model="userQuery.loginPwd"/>
      </el-form-item>

      <el-form-item label="密码" prop="loginPwd" v-else><!--新增-->
        <el-input type="password" v-model="userQuery.loginPwd"/>
      </el-form-item>

      <el-form-item label="姓名" prop="name">
        <el-input v-model="userQuery.name"/>
      </el-form-item>

      <el-form-item label="手机" prop="phone">
        <el-input v-model="userQuery.phone"/>
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input v-model="userQuery.email"/>
      </el-form-item>

      <el-form-item label="账号未过期" prop="accountNoExpired">
        <el-select v-model="userQuery.accountNoExpired" placeholder="请选择">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </el-form-item>

      <el-form-item label="密码未过期" prop="credentialsNoExpired">
        <el-select v-model="userQuery.credentialsNoExpired" placeholder="请选择">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </el-form-item>

      <el-form-item label="账号未锁定" prop="accountNoLocked">
        <el-select v-model="userQuery.accountNoLocked" placeholder="请选择">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </el-form-item>

      <el-form-item label="账号是否启用" prop="accountEnabled">
        <el-select v-model="userQuery.accountEnabled" placeholder="请选择">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="userDialogVisible = false">关 闭</el-button>
        <el-button type="primary" @click="userSubmit">提 交</el-button>
      </span>
    </template>

  </el-dialog>

  <el-table
      :data="userList"
      style="width: 100%"
      @selection-change="handleSelectionChange"
  >
    <el-table-column type="selection" width="55"/>
    <el-table-column type="index" label="序号" width="120"/>
    <el-table-column property="loginAct" label="账号" width="120"/>
    <el-table-column property="name" label="姓名" show-overflow-tooltip/>
    <el-table-column property="phone" label="联系方式" show-overflow-tooltip/>
    <el-table-column property="email" label="邮箱" show-overflow-tooltip/>
    <el-table-column property="createTime" label="创建时间" show-overflow-tooltip/>

    <el-table-column label="操作" show-overflow-tooltip>
      <template #default="scope">
        <el-button type="primary" @click="view(scope.row.id)">详情</el-button>
        <el-button type="success" @click="edit(scope.row.id)">编辑</el-button>
        <el-button type="danger" @click="del(scope.row.id)">删除</el-button>
      </template>

    </el-table-column>

  </el-table>


  <el-pagination
      background
      layout="prev, pager, next"
      :page-size="pageSize"
      :total="total"
      @prev-click="toPage"
      @current-change="toPage"
      @next-click="toPage"
  />
</template>

<script>
import {defineComponent} from "vue";
import {doDelete, doGet, doPost, doPut} from "../http/httpRequest.js";
import {messageConfirm, messageTip} from "../util/utils.js";

export default defineComponent({
  name: "UserView",
  data() {
    return {
      //用户列表数据，初始值是空
      userList: [{}],
      //分页时每页显示多少条数据
      pageSize: 0,
      //分页总共查询出多少条数据
      total: 0,
      //用户弹窗是否弹出
      userDialogVisible: false,
      //定义表单对象初始值为空
      userQuery: {},
      //定义用户验证规则
      userRules: {
        loginAct: [
          {required: true, message: '请输入登录账号', trigger: 'blur'}
        ],
        loginPwd: [
          {required: true, message: '请输入登录密码', trigger: 'blur'},
          {min: 6, max: 16, message: '登录密码长度为6-16位', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'},
          {pattern: /^[\u4E00-\u9FA5]{1,5}$/, message: '姓名必须是中文', trigger: 'blur'}
        ],
        phone: [
          {required: true, message: '请输入手机号码', trigger: 'blur'},
          {pattern: /^1[3-9]\d{9}$/, message: '手机号码格式有误', trigger: 'blur'}
        ],
        email: [
          {required: true, message: '请输入邮箱', trigger: 'blur'},
          {pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: '邮箱格式有误', trigger: 'blur'}
        ],
        accountNoExpired: [
          {required: true, message: '请选择账号是否未过期', trigger: 'blur'},
        ],
        credentialsNoExpired: [
          {required: true, message: '请选择密码是否未过期', trigger: 'blur'},
        ],
        accountNoLocked: [
          {required: true, message: '请选择账号是否未未锁定', trigger: 'blur'},
        ],
        accountEnabled: [
          {required: true, message: '请选择账号是否可用', trigger: 'blur'},
        ]
      },
      //下拉选项数组
      options: [
        {label: '是', value: 1},
        {label: '否', value: 0}
      ],
      //用户id的数组
      userIdArray: []
    }
  },

  mounted() {
    this.getData(1);
  },

  methods: {
    //勾选或者取消勾选时，触发该函数(已经勾选的数据或传给我们这个函数)
    handleSelectionChange(selectionDataArray) {
      this.userIdArray = [];
      console.log(selectionDataArray);
      selectionDataArray.forEach(data => {
        let userId = data.id;
        this.userIdArray.push(userId);
      })
    },

    //新增用户提交保存
    userSubmit() {
      let formData = new FormData();
      for (let field in this.userQuery) {
        formData.append(field, this.userQuery[field]);
      }
      this.$refs.userRefForm.validate((isValid) => {
        if (isValid) {

          if (this.userQuery.id > 0) { /*编辑*/
            doPut("/api/user", formData).then(resp => {
              if (resp.data.code === 200) {
                messageTip("编辑成功", "success");
                //页面刷新
                this.reload();
              } else {
                messageTip("编辑失败", "error");
              }
            })
          } else {
            doPost("/api/user", formData).then(resp => {/*新增*/
              if (resp.data.code === 200) {
                messageTip("提交成功", "success");
                //页面刷新
                this.reload();
                console.log(this.age)
                console.log(this.content)
                console.log(this.arr)
                console.log(this.user)
              } else {
                messageTip("提交失败", "error");
              }
            })
          }
        }
      })
    },

    //查询用户列表数据
    getData(current) {
      doGet("/api/users", {
        //当前查询第几页
        current: current
      }).then(resp => {
        console.log(resp);
        if (resp.data.code === 200) {
          this.userList = resp.data.data.list;
          this.pageSize = resp.data.data.pageSize;
          this.total = resp.data.data.total;
        }
      })
    },

    toPage(current) {
      this.getData(current);
    },

    view(id) {
      console.log(this.userList);
      this.$router.push("/dashboard/user/" + id);
    },

    add() {
      this.userQuery = {};
      this.userDialogVisible = true;
    },

    //编辑用户
    edit(id) {
      this.userDialogVisible = true;
      this.loadUser(id);
    },

    //删除
    del(id) {
      messageConfirm("您确定要删除该数据吗？").then(() => { //用户点击“确定”按钮就会触发then函数
        doDelete("/api/user/" + id, {}).then(resp => {
          console.log(resp);
          if (resp.data.code === 200) {
            messageTip("删除成功", "success");
            //页面刷新
            this.reload();
          } else {
            messageTip("删除失败，原因：" + resp.data.msg, "error");
          }
        })
      }).catch(() => { //用户点击“取消”按钮就会触发catch函数
        messageTip("取消删除", "warning");
      })
    },

    //批量删除
    batchDel() {
      if (this.userIdArray.length <= 0) {
        //提示一下
        messageTip("请选择要删除的数据", "warning");
        return;
      }
      messageConfirm("您确定要删除这些数据吗？").then(() => { //用户点击“确定”按钮就会触发then函数
        //原来是数组：[1,3,5,6,7,11,15]  --> ids = "1,3,5,6,7,11,15";
        let ids = this.userIdArray.join(",");
        doDelete("/api/user", {
          ids: ids
        }).then(resp => {
          console.log(resp);
          if (resp.data.code === 200) {
            messageTip("批量删除成功", "success");
            //页面刷新
            this.reload();
          } else {
            messageTip("批量删除失败，原因：" + resp.data.msg, "error");
          }
        })
      }).catch(() => { //用户点击“取消”按钮就会触发catch函数
        messageTip("取消批量删除", "warning");
      })
    },

    //加载用户信息
    loadUser(id) {
      doGet("/api/user/" + id, {}).then(resp => {
        if (resp.data.code === 200) {
          this.userQuery = resp.data.data;
          this.userQuery.loginPwd = "";
        }
      })
    },
  }
})

</script>


<style scoped>

</style>
