import {ElMessage} from "element-plus";

export function messageTip(msg, type) {
    ElMessage({
        showClose: false,
        message: msg,
        type: type,
        center: true,
        duration: 1000
    })
}
