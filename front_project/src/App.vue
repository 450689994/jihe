<template>
  <div id="login">
    <router-view id="routerArea"> </router-view>
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      imgSrc: require("./static/image/leone-venter-VieM9BdZKFo-unsplash.jpg"),
    };
  },
};
</script>

<style scoped>
#login {
  background: url("./static/image/leone-venter-VieM9BdZKFo-unsplash.jpg")
    no-repeat;
  background-position: center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}

#routerArea{
  position: absolute;
  left: 50%;
  top: 50%;
}
</style>