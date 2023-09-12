import { useMessage } from "naive-ui"

export default function() {
    const size = "large"
    const maxLen = "50"
    const minNameLen = 8
    const maxNameLen = 30
    const minPasswordLen = 8
    const maxPasswordLen = 30
    const userComment = "长度" + minNameLen + " - " + maxNameLen
    const passwordComment = "长度" + minPasswordLen + " - " + maxPasswordLen

    const message = useMessage()

    return {
        size,
        maxNameLen,
        minNameLen,
        maxPasswordLen,
        minPasswordLen,
        maxLen,
        passwordComment,
        message,
        userComment
    }

}