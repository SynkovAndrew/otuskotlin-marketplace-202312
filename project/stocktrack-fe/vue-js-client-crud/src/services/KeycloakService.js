import Keycloak from "keycloak-js";

const keycloakInstance = new Keycloak();

function Login (callback) {
    keycloakInstance
        .init({ onLoad: "login-required" })
        .then(function (authenticated) {
            authenticated ? callback() : alert("non authenticated");
        })
        .catch((e) => {
            console.dir(e);
            console.log(`keycloak init exception: ${e}`);
        });
}

const UserName = () => keycloakInstance?.tokenParsed?.preferred_username;

const Token = () =>  keycloakInstance?.token;

const LogOut = () => keycloakInstance.logout();

const KeyCloakService = {
    CallLogin: Login,
    GetUserName: UserName,
    GetAccessToken: Token,
    CallLogOut: LogOut,
};

export default KeyCloakService;