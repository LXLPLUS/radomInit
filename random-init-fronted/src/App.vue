<template>
  <n-message-provider>
    <n-config-provider :hljs="hljs">
      <Menu/>
    </n-config-provider>
  </n-message-provider>
</template>
<script>
import Menu from "./components/Menu.vue";
import { defineComponent } from 'vue'
import hljs from 'highlight.js/lib/core'
import javascript from 'highlight.js/lib/languages/javascript'

hljs.registerLanguage('javascript', javascript)

export default defineComponent({
  name: "App",
  components: {
    Menu
  },
  setup() {
    return {
      hljs
    }
  }
})
</script>
