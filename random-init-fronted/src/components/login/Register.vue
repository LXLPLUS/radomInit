<template>
  <n-form
      label-placement="left"
      label-width="auto"
      label-align="left"
      :show-require-mark="true"
      require-mark-placement="right">
    <n-space vertical>
      <n-form-item label="账号">
        <n-input :size='loginFunc.size'
                 round
                 :placeholder="loginFunc.userComment"
                 v-model:value='modelRef.userName'
                 :maxlength='loginFunc.maxNameLen'
                 :show-count=true
        />
      </n-form-item>

      <n-form-item label="密码">
        <n-input :size='loginFunc.size'
                 :placeholder='loginFunc.passwordComment'
                 round
                 :maxlength='loginFunc.maxPasswordLen'
                 :show-count=true
                 v-model:value="modelRef.userPassword"
                 type="password"
        />
      </n-form-item>


      <n-form-item label="确认">
      <n-input :size='loginFunc.size'
               placeholder="重复输入密码确认"
               round
               :maxlength='loginFunc.maxPasswordLen'
               :show-count=true
               v-model:value="modelRef.reenteredPassword"
               type="password"
      />
      </n-form-item>

    </n-space >
    <n-space justify="space-around">
      <n-button type="default" dashed @click="clear">
        刷新
      </n-button>
      <n-button type="success" @click="login">
        注册
      </n-button>
    </n-space>
  </n-form>
</template>

<script lang="ts">
import { defineComponent, ref} from 'vue'
import LoginHeader from "./LoginHeader.vue";
import loginHook from "../../hooks/loginHook.js"

export default defineComponent({
  name: "Register",
  components: {LoginHeader},

  setup() {
    const loginFunc = loginHook()

    let modelRef = ref({
      userName: "",
      userPassword: "",
      reenteredPassword: ""
    });

    async function login() {

      if (modelRef.value.userName.length < loginFunc.minNameLen) {
        loginFunc.message.warning("用户名长度太短，最短为" + loginFunc.minNameLen + "位")
        return
      }
      if (modelRef.value.userPassword.length < loginFunc.minPasswordLen) {
        loginFunc.message.warning("密码长度太短，最短为" + loginFunc.minPasswordLen + "位")
        return
      }
      if (modelRef.value.reenteredPassword !== modelRef.value.userPassword) {
        loginFunc.message.warning("两次输入的密码不一致")
        return
      }
      const config = {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          "userName": modelRef.value.userName,
          "password": modelRef.value.userPassword
        })
      }
      const result = await fetch("/backend/createUser", config)
          .then(data => data.json())

      if ( typeof result == "undefined" ) {
        loginFunc.message.warning("后端返回失败")
        return
      }
      if (result.errorCode !== 0) {
        const { errorMessage } = result
        loginFunc.message.warning(errorMessage, { duration: 5000 })
        return
      }

      loginFunc.message.info(modelRef.value.userName + "成功注册，欢迎", { duration: 5000 })
    }
    function clear() {
      modelRef.value.userName = ""
      modelRef.value.userPassword = ""
      modelRef.value.reenteredPassword = ""
    }

    return {
      loginFunc,
      modelRef,
      login,
      clear
    };
  }
})
</script>