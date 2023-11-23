import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from "@/components/layout/Layout"
import Me from "@/views/me/Index"

Vue.use(VueRouter);

const routes = [

    {
        path: '/login',
        name: 'Login',
        component: () => import("@/views/Login")
    },

    {
        path: '/register',
        name: 'Register',
        component: () => import("@/views/Register")
    },

    //公共布局下的路由
    {
        path: '/',
        component: Layout,
        children: [
            {
                path: '',
                component: () => import("@/views/Home")
            },

            {
                path: '/films',
                component: () => import("@/views/Films")
            },

        ]
    }
];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
});

export default router
