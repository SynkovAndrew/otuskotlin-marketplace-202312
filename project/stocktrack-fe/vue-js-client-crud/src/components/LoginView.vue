<template>
  <div class="login">
    <h2>Username: {{ Login() }}</h2>
    <h2>Access Token: {{ AccessToken() }}</h2>
    <button @click="LogOut">Log Out</button>
  </div>
</template>

<script>
import { defineComponent } from "vue";
import KeyCloakService from "../services/KeycloakService.js";

export default defineComponent({
  name: "LoginView",
  methods: {
    Login() {
      return KeyCloakService.GetUserName();
    },
    AccessToken() {
      return KeyCloakService.GetAccessToken();
    },
    LogOut() {
      return KeyCloakService.CallLogOut();
    },
  },
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>