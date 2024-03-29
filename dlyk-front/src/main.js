import {createApp} from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from "./router/router.js";
import App from "./App.vue";
import * as ElementPlusIconsVue from '@element-plus/icons-vue'


const app = createApp(App);

//注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(ElementPlus).use(router).mount('#app')
