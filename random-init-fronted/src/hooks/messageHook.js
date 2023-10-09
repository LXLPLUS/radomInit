import {createDiscreteApi} from "naive-ui";

export default function () {
    const { message, dialog } = createDiscreteApi(
        ["message", "dialog"]
    );

    return {
        message,
        dialog
    }
}
