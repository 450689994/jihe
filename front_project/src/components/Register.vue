<template>
  <table>
    <tr>
      <td>用户名：</td>
      <td>
        <el-input
          v-model="username"
          placeholder="请输入内容"
          @keyup.native="checkName"
        ></el-input>
        <span v-if="username_sgin && username != ''"><font color="red">用户名不可用</font></span>
        <span v-if="!username_sgin && username != ''"><font color="green">用户名可用</font></span>
      </td>
    </tr>
    <tr>
      <td>密码：</td>
      <td>
        <el-input
          placeholder="请输入密码"
          v-model="password"
          show-password
        ></el-input>
      </td>
    </tr>
    <tr>
      <td>确认密码：</td>
      <td>
        <el-input
          placeholder="请输入密码"
          v-model="confirmPassword"
          show-password
        ></el-input>
        <span v-if="password != confirmPassword">
          <font color="red">两次密码不一致</font>
        </span>
        <span v-else-if="password!=''">
          <font color="green">√</font>
        </span>
      </td>
    </tr>
    <tr>
      <td><el-button type="primary" @click="register">注册</el-button></td>
      <td>
        <router-link to="/login">去登录...</router-link>
      </td>
    </tr>
  </table>
</template>

<script>
import axios from "axios";
import md5 from "js-md5";
export default {
  data() {
    return {
      username: "",
      password: "",
      confirmPassword: "",
      username_sgin: "", //username标志位
    };
  },
  methods: {
    register: function () {
      
      if (this.password_sgin == false) {
        return;
      }
      var user = {
        username: this.username,
        password: md5(this.password),
      };
      const url = "http://43.128.13.32:9999/register";
      axios.post(url, user).then((response) => {
        console.log(response)
        if (response.data.message == "失败") {
          alert("注册失败！是不是偷偷关闭了js！");
        } else {
          if (confirm("注册成功，点击确定进入登录页面！")) {
            this.$router.push("login");
          }
        }
      });
    },
    checkName: function () {
      var url =
        "http://43.128.13.32:9999/checkName?username=" + this.username;
      axios.get(url).then((response) => {
        if (response.data.message == "失败") {
          this.username_sgin = true;
        } else {
          this.username_sgin = false;
        }
      });
    },
  },
};
</script>

<style>
</style>