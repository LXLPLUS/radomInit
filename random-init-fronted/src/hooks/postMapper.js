import {useMessage} from "naive-ui"
import router from "../router/router";
import messageHook from "./messageHook"

export default function () {

    async function post(url, body) {

        const message = messageHook().message

        const config = {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        }

        return await fetch(url, config).then(data => {
            if (data.status === 401) {
                useMessage().warning("未登录，请登录")
                router.replace({
                    path: "/LoginAndRegister"
                })
                return {}
            }
            if (data.status !== 200) {
                message.warning("后端错误")
                return {}
            }
            return data.json()
        }).then(data => {
            if (data.errorCode !== 0) {
                message.warning(data["errorMessage"])
                return {}
            }
            return data.data
        }).catch(() => {
                message.warning("后端无响应")
                return {}
            }
        )
    }

    return {
        post
    }
}