//引入
import VueRouter from "vue-router";

//引入路由管理的组件
import Login from '../components/Login.vue';
import Register from '../components/Register.vue';
import Main from '../components/Main.vue';

//创建vue-router，并暴露出去

export default new VueRouter({
    routes:[
        {
            path:'/',   //默认页面
            component:Login
        },
        {
            path:'/login',
            component:Login
        },
        {
            path:'/register',
            component:Register
        },
        {
            path:'/main',
            component:Main
        }
    ]
})