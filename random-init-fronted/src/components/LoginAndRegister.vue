<template>
  <n-grid x-gap="12" :cols="3">
    <n-gi>
      <div style="text-align: right">
        <LoginHeader/>
      </div>
    </n-gi>
    <n-gi>
      <n-layout>
        <n-card style="text-align: center">
          <n-tabs class="card-tabs"
                  default-value="login"
                  size="large"
                  animated
                  justify-content="start"
                  type="line"
          >
            <n-tab-pane name="login" tab="登录">
              <Login/>
            </n-tab-pane>
            <n-tab-pane name="register" tab="注册">
              <Register/>
            </n-tab-pane>
          </n-tabs>
        </n-card>
      </n-layout>
    </n-gi>
  </n-grid>

</template>

<script>
import Login from "./Login.vue";
import Register from "./Register.vue";
import LoginHeader from "./LoginHeader.vue"
export default {
  name: "LoginAndRegister",
  components: {
    Login,
    Register,
    LoginHeader
  }

}
</script>