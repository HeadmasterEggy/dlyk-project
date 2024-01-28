import {ElMessage} from "element-plus";

/**
 * 消息提示
 * @param msg
 * @param type
 */
export function messageTip(msg, type) {
    ElMessage({
        showClose: false,
        message: msg,
        type: type,
        center: true,
        duration: 1000
    })
}

/**
 * 获取存储在localStorage或sessionStorage中的token的名字
 * @returns {string}
 */
export function getTokenName() {
    return "dlyk token";
}

export function removeToken() {
    window.localStorage.removeItem(getTokenName());
    window.sessionStorage.removeItem(getTokenName());
}
