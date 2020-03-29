import Menu from "../components/Menu"
import Content from "../components/Content"

export default {
  install(Vue) {
    Vue.component(Menu.name, Menu);
    Vue.component(Content.name, Content)
  }
};
