import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import KeyCloakService from "./services/KeycloakService.js";
import Http from "./http";

const renderApp = () => {
    createApp(App).use(router).mount("#app");
};

KeyCloakService.CallLogin(renderApp);
Http.configureAxiosKeycloak();
