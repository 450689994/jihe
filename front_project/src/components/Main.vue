<template>
  <div>
    <h1>Welcome to TaoChenArea</h1>
    <br />
    <a href="#" @click="other">点击发送其他请求（token有效期30s）</a>
  </div>
</template>

<script type="text/javascript">
import axios from "axios";
export default {
  methods: {
    other: function () {
      var token = JSON.parse(localStorage.getItem("token"));
      axios({
        url: "http://43.128.13.32:9999/other",
        method: "get",
        headers: {
          token: token,
        },
      }).then((response) => {
        if (response.data.success == false) {
          alert("登录时效，请重新登录");
          this.$router.push("login");
        } else {
          alert("请他请求成功~");
        }
      });
    },
  },
};
</script>

<style>
</style>