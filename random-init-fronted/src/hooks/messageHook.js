import {createDiscreteApi} from "naive-ui";

export default function () {
    const { message } = createDiscreteApi(
        ["message"]
    );

    return {
        message
    }
}
