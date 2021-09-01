import Vue from 'vue'
import App from './App.vue'

import VueRounter from 'vue-router'
import router from './router/index'

import ElementUI from 'element-ui';

import 'element-ui/lib/theme-chalk/index.css';

Vue.config.productionTip = false

Vue.component(VueRounter);
Vue.use(ElementUI);

new Vue({
  render: h => h(App),
  router: router
}).$mount('#app')

