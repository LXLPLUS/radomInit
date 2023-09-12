import {reactive} from "vue";

export default function () {
    let config = reactive({
        size: "large",
        maxLen: "50"
    })

    return  {config}
}