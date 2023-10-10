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

    </n-space >
    <n-space justify="space-around">
      <n-button type="default" dashed @click="clear">
        刷新
      </n-button>
      <n-button type="info" @click="login">
        登录
      </n-button>
    </n-space>
  </n-form>
</template>

<script lang="ts">
import { defineComponent, ref} from 'vue'
import LoginHeader from "./LoginHeader.vue";
import loginHook from "../../hooks/loginHook.js"
import { useRouter } from "vue-router";


export default defineComponent( {
  name: "Login",
  components: {
    LoginHeader
  },
  setup() {

    const router = useRouter()

    const loginFunc = loginHook()

    let modelRef = ref({
      userName: "",
      userPassword: ""
    });

    function clear() {
      modelRef.value.userName = ""
      modelRef.value.userPassword = ""
    }

    async function login() {
      if (modelRef.value.userName.length < loginFunc.minNameLen) {
        loginFunc.message.warning("用户名长度太短，最短为" + loginFunc.minNameLen + "位")
        return
      }
      if (modelRef.value.userPassword.length < loginFunc.minPasswordLen) {
        loginFunc.message.warning("密码长度太短，最短为" + loginFunc.minPasswordLen + "位")
        return
      }

      const config = {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({"userName": modelRef.value.userName, "password" : modelRef.value.userPassword})
      }

      const response = await fetch("/backend/login", config)
          .then(data => data.json()).catch((e) => console.log(e))

      if (response === null || response === void 0) {
        loginFunc.message.error("后端响应失败！")
        return
      }
      if (response.errorCode !== 0) {
        const { errorMessage } = response
        loginFunc.message.warning(errorMessage)
        return
      }
      loginFunc.message.success("欢迎 " + modelRef.value.userName)
      await router.push("/tableCreate");
    }

    return {
      loginFunc,
      modelRef,
      clear,
      login
    }
  }
})
</script>