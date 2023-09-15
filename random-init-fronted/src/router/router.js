import { createRouter, createWebHashHistory } from "vue-router"
import LoginAndRegister from "../components/LoginAndRegister.vue"
import Regex from "../components/Regex.vue"
import Enum from "../components/Enum.vue"
import BuilderRuler from "../components/BuilderRuler.vue"
import TableCreate from "../components/TableCreate.vue"

const routes = [
    {
        path: "/LoginAndRegister",
        component: LoginAndRegister
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
    }
]

const router = createRouter({
    history:createWebHashHistory(),
    routes,
})

export default router