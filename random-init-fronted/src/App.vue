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
import java from 'highlight.js/lib/languages/java'

hljs.registerLanguage('java', java)

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
