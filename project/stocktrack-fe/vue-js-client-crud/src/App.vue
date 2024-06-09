<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark bg-dark">
      <div class="navbar-nav mr-auto">
        <li class="nav-item">
          <router-link to="/stocks" class="nav-link">Stocks</router-link>
        </li>
      </div>
      <div>
        <router-link to="/auth-details" class="nav-link" style="color: #f1f1f1">{{ Login() }}</router-link>
        <button type="button" class="btn btn-light" @click="LogOut">Logout</button>
      </div>
    </nav>

    <div>
      <router-view />
    </div>
  </div>
</template>

<script>
import KeyCloakService from "./services/KeycloakService.js";

export default {
  name: 'App',
  components: {

  },
  methods: {
    Login() {
      return KeyCloakService.GetUserName();
    },
    LogOut() {
      return KeyCloakService.CallLogOut();
    },
  },
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.h5, h5 {
  font-size: 1.25rem;
  color: white;
}
</style>
