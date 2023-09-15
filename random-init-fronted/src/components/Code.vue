<template>
  <n-button strong secondary style="text-align: right" type="info" @click="copy">
    复制到剪贴板
  </n-button>
  <br>
  <n-card style="min-height: 800px" :embedded="true">
    <n-code v-model:code="this.props.code" language="javascript" show-line-numbers/>
  </n-card>
</template>

<script>
import { useMessage } from "naive-ui";
import clipboard3 from 'vue-clipboard3';

export default {
  name: "Code",
  props:[
      "code"
  ],
  setup(props) {
    const message = useMessage();
    const { toClipboard } = clipboard3();

    async function copy() {
      console.log(props.code)
      try {
        await toClipboard(props.code)
        message.info("复制成功")
      } catch (Error) {
        message.warning("复制失败")
      }
    }

    return {
      props,
      copy
    }
  }

}
</script>

<style scoped>

</style>