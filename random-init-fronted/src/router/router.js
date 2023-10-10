import { createRouter, createWebHashHistory } from "vue-router"
import LoginAndRegister from "../components/login/LoginAndRegister.vue"
import Regex from "../components/Regex.vue"
import Enum from "../components/Enum.vue"
import BuilderRuler from "../components/BuilderRuler.vue"
import TableCreate from "../components/tableGather/TableCreate.vue"
import {
    createDiscreteApi
} from 'naive-ui'
import logExplain from "../components/login/LogExplain.vue";
import TableList from "../components/TableList/TableList.vue";
const { message } = createDiscreteApi(
    ["message"]
);

const routes = [
    {
        path: "/LoginAndRegister",
        component: LoginAndRegister,
        meta: {
            mustLogin:false
        }
    },
    {
        path: "/regex",
        component: Regex
    },
    {
        path: "/enum",
        component: Enum
    },
    {
        path: "/builderRuler",
        component: BuilderRuler
    },
    {
        path: "/tableCreate",
        component: TableCreate
    },
    {
        path: "/logExplain",
        component: logExplain
    },
    {
        path: "/tableList",
        component: TableList
    }
]

const router = createRouter({
    history:createWebHashHistory(),
    routes,
})

router.beforeEach(async (to, from, next) => {
    // 如果登录首页，自动跳转到登录界面
    if (to.path === "/") {
        return next("/LoginAndRegister")
    }
    // 校验是否是必须登录端口
    if (to.meta["mustLogin"] === false) {
        return next();
    }
    const response = await fetch("/backend/loginCheck").then(data => data.json()).catch(() => message.warning("网络错误"))

    if (response["data"] === void 0 || response.data.login === void 0 || response.data.login === false) {
        message.warning("未登录！")
        return next("/LoginAndRegister")
    }
    if (response.data.login === true) {
        return next()
    }
    message.warning("网络错误")
    return next("/LoginAndRegister")
})

export default router