import { useMessage } from "naive-ui";
import router from "../router/router";

export default function () {

    function params(obj) {
        let result = '';
        let item;
        for (item in obj) {
            if(obj[item] && String(obj[item])) {
                result += `&${item}=${obj[item]}`;
            }
        }
        if (result) {
            result ='?'+ result.slice(1);
        }
        return result;
    }

    async function get(url, paramMap) {

        const message = useMessage()

        url = url + params(paramMap)
        return await fetch(url).then(data => {
            if (data.status === 401) {
                message.warning("未登录，请登录")
                router.replace({
                    path: "/LoginAndRegister"
                })
                return {}
            }
            if (data.status === void 0) {
                message.warning("网络错误")
            }
            if (data.status !== 200) {
                message.warning("后端错误")
                return {}
            }
            return data.json()
        }).then((data) => {
            if (data.errorCode !== 0) {
                message.warning(data["errorMessage"])
                return {}
            }
            return data.data
        })
    }

    return {
        params,
        get
    }
}

