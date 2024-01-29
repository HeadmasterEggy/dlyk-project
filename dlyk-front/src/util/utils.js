import {ElMessage, ElMessageBox} from "element-plus";

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
 * 消息确认提示
 *
 * @param msg
 * @returns {Promise<MessageBoxData>}
 */
export function messageConfirm(msg) {
    return ElMessageBox.confirm(
        msg, //提示语
        '系统提醒', //提示的标题
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
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
