import axios from "axios";
import KeyCloakService from "./services/KeycloakService.js";

/*
export default axios.create({
    baseURL: "http://localhost:8090/api/v1",
    headers: {
        "Content-type": "application/json"
    }
});
*/

const HttpMethods = {
    GET: "GET",
    POST: "POST",
    DELETE: "DELETE",
};

const _axios = axios.create({
    baseURL: "http://localhost:8090/api/v1",
    headers: {
        "Content-type": "application/json"
    }
});

function cb(config) {
    config.headers.Authorization = `Bearer ${KeyCloakService.GetAccessToken()}`;
    return config;
}

const configureAxiosKeycloak = () => {
    _axios.interceptors.request.use(function (config) {
            if (KeyCloakService.IsLoggedIn()) {
                KeyCloakService.UpdateToken(cb(config));
            }

            return config;
        }
    );
};

const getAxiosClient = () => _axios;

const Http = {
    HttpMethods,
    configureAxiosKeycloak,
    getAxiosClient,
};

export default Http;