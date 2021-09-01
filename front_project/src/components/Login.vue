<template>
  <div>
      <table>
          <tr>
              <td>账号：</td>
              <td><el-input v-model="username" placeholder="请输入内容"></el-input></td>
          </tr>
          <tr>
              <td>密码：</td>
              <el-input placeholder="请输入密码" v-model="password" show-password></el-input>
          </tr>
          <tr>
              <td><el-button type="primary" @click="login">登录</el-button></td>
              <td>
                  <router-link to="/register">去注册...</router-link>
              </td>
          </tr>
      </table>
        <br>
        <a href="https://github1s.com/450689994/jihe">点击此处通过在线编辑器打开项目于（GitHub1s）</a><br>
    <a href="https://github.com/450689994/jihe">点击此处进入GitHub打开项目</a>
  </div>
</template>

<script>
    import Vue from 'vue'
    import VueRounter from 'vue-router'
    import md5 from 'js-md5';
    import axios from 'axios';
    Vue.use(VueRounter)
    export default {
        data(){
            return{
                username:'',
                password:''
            }
        },
        methods:{
            login:function () {
                var user = 
                {
                    "username":this.username,
                    "password":md5(this.password)
                }
                const url = 'http://localhost:8080/back/login';
                axios.post(url,user).then(
                    response => {
                       if(response.data.message == '失败'){
                           alert("账号或密码错误");
                       }else{
                           //保存token(string类型)
                           localStorage.setItem('token',JSON.stringify(response.data.data));
                           if(confirm("登录成功，点击确定进入主页面！")){
                               this.$router.push('main')
                           }
                       }
                    }
                )
            }
        }
    }
</script>

<style>

</style>