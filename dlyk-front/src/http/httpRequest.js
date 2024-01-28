import axios from "axios";
import {getTokenName, messageTip, removeToken} from "../util/utils.js";
import {ElMessageBox} from "element-plus";
//定义后端接口地址的前缀
axios.defaults.baseURL = "http://localhost:8089"

export function doGet(url, params) {
    return axios({
        method: "GET",
        url: url,
        params: params,
        dataType: "json"
    })
}

export function doPost(url, data) {
    return axios({
        method: "POST",
        url: url,
        data: data,
        dataType: "json"
    })
}

export function doPut(url, data) {
    axios({
        method: "PUT",
        url: url,
        data: data,
        dataType: "json"
    }).then(function (rep) {
        var s = "";
        rep.data.forEach(function (stu) {
            s += stu.name + "---------------" + stu.age + "<br>";
        });
        document.getElementById("mydiv").innerHTML = s;
    })
}

export function doDelete(url, params) {
    axios({
        method: "DELETE",
        url: url,
        params: params,
        dataType: "json"
    }).then(function (rep) {
        var s = "";
        rep.data.forEach(function (stu) {
            s += stu.name + "---------------" + stu.age + "<br>";
        });
        document.getElementById("mydiv").innerHTML = s;
    })
}

// 添加请求拦截器
axios.interceptors.request.use((config) => {
    // 在请求头中放一个token（jwt）传给后端
    let token = window.sessionStorage.getItem(getTokenName());
    if (!token) {
        token = window.localStorage.getItem(getTokenName());
    } else {
        config.headers['Authentication'] = token;
    }
    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// 添加响应拦截器
axios.interceptors.response.use((response) => {
    // 2xx 范围内的状态码都会触发该函数。
    if (response.data.data > 900) {
        ElMessageBox.confirm(
            response.data.message + '是否重新登录?',
            'Warning',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }
        )
            .then(() => {
                //后端token未通过,前端token肯定有问题, 所以没必要存到浏览器中
                removeToken();
                window.location.href = "/";
            })
            .catch(() => {
                messageTip("取消登录", "warning");
            })
    }
    return response;
}, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
});
