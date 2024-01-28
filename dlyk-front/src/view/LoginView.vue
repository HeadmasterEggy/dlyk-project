<template>
  <el-container>
    <!--左侧-->
    <el-aside width="200px">
      <img src="../assets/img.png">
      <p class="imgTitle">
        欢迎使用系统
      </p>
    </el-aside>

    <!--右侧-->
    <el-main>
      <div class="loginTitle">欢迎登录</div>

      <el-form ref="loginRefForm" :model="user" label-width="120px" :rules="LoginRules">
        <el-form-item label="账号" prop="loginAct">
          <el-input v-model="user.loginAct"/>
        </el-form-item>

        <el-form-item label="密码" prop="loginPwd">
          <el-input type="password" v-model="user.loginPwd"/>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="login">登录</el-button>
        </el-form-item>


        <el-form-item>
          <el-checkbox label="记住我" name="rememberMe"/>
        </el-form-item>

      </el-form>

    </el-main>
  </el-container>
</template>

<script>
import {doPost} from "../http/httpRequest.js";
import {ElMessage} from "element-plus";
import {messageTip} from "../util/utils.js";

export default {
  name: "LoginView",
  data() {
    return {
      //对象变量定义
      user: {},
      //字符串变量定义
      name: '',
      //数字变量定义
      age: 0,
      //数组变量定义
      arr: [],
      //list集合对象
      userList: [{}],
      //定义登录表单的验证规则
      LoginRules: {
        loginAct: [
          {required: true, message: '请输入登录账号', trigger: 'blur'}
        ],
        loginPwd: [
          {required: true, message: '请输入登录密码', trigger: 'blur'},
          {min: 6, max: 16, message: '登录密码长度为6-16位', trigger: 'blur'}
        ]
      }
    }
  },

  methods: {
    //登录函数
    login() {
      //提交前保证输入框的合法性
      this.$refs.loginRefForm.validate((isValid) => {
        if (isValid) {
          let formData = new FormData();
          formData.append("loginAct", this.user.loginAct);
          formData.append("loginPwd", this.user.loginPwd);
          doPost("/api/login", formData).then((resp) => {
            console.log(resp);
            if (resp.data.code === 200) {
              messageTip("登录成功", 'success');
              //跳转体统主界面
              window.location.href = "/dashboard";
            } else {
              messageTip("账号或密码错误", 'error');
            }
          });
        }
      })
    }
  }
}
</script>


<style scoped>
.el-aside {
  background: black;
  width: 40%;
  text-align: center;
}

.el-main {
  height: calc(100vh);
}

img {
  width: 50vh;
}

.imgTitle {
  color: white;
  font-size: 28px;
}

.el-form {
  width: 60%;
  margin: auto;
}

.loginTitle {
  text-align: center;
  margin-top: 100px;
  margin-bottom: 15px;
  font-weight: bold;
}

.el-button {
  width: 100%;
}
</style>
