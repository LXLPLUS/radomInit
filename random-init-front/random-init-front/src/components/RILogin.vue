<template>
  <div>
    <n-space vertical>
      <head-logo/>
      <n-input :size='size' round placeholder="输入用户名,最大长度50" v-bind:maxlength='maxLen' :show-count=true type="test" clearable/>
      <n-input :size='size' round placeholder="密码，最小长度为8" v-bind:maxlength='maxLen' :show-count=true type="password" clearable/>
      <n-input :size='size' round placeholder="重复输入密码确认" v-bind:maxlength='maxLen' :show-count=true type="password" clearable/>
      <br />
    </n-space>
    <n-button type="tertiary">
      刷新
    </n-button>
    <n-button type="info">
      登录
    </n-button>
  </div>
</template>

<script>
import {defineComponent} from 'vue'
import headLogo from "@/components/headLogo";
import UseLogin from "@/hooks/UseLogin"


export default defineComponent({
  components:{
    headLogo
  },
  name: "RILogin",
  setup() {
    let hook = UseLogin()

    let size = hook.size
    let maxLen = hook.maxLen
    return { size, maxLen };
  }
})
</script>