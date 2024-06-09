<template>
  <div class="login">
    <h3>Username: {{ Login() }}</h3>
    <button @click="ShowAccessToken">Access Token</button>
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
    ShowAccessToken() {
      alert(KeyCloakService.GetAccessToken())
    }
  },
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
button {
  margin: 40px 0 0;
}
</style>