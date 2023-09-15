<template>
  <n-grid x-gap="12" :cols="2">
    <n-gi>
      <div>正则表达式输入:</div>
      <n-input
          v-model:value="regex"
          type="textarea"
          :placeholder="defaultInput"
      />
      <br/>
      <br/>
      <n-grid x-gap="12" :cols="8">
        <n-gi>
          <h3>生成数量</h3>
        </n-gi>
        <n-gi :span="7">
          <n-slider v-model:value="count" :marks="marks" :max="50"/>
        </n-gi>
      </n-grid>
      <n-space justify="space-around">
        <n-button type="info" dashed @click="checkRegex">
          验证
        </n-button>
        <n-button type="success" @click="intoDB">
          入库
        </n-button>
      </n-space>
      <RegexRuler/>
    </n-gi>
    <n-gi>
      <Code v-model:code="code"/>
    </n-gi>
  </n-grid>
</template>

<script lang="ts">
import { ref } from "vue"
import { useMessage } from "naive-ui";
import Code from "./Code.vue"
import RegexRuler from "./RegexRuler.vue"

export default {
  name: "Regex",
  components: {Code, RegexRuler},
  setup() {
    const message = useMessage()
    let defaultInput = ref("[0-9]{1,12}")
    let code = ref("12312")
    let regex = ref("")
    let count = ref(10)

    const marks = {
      10: '10',
      20: '20',
      30: '30',
      40: '40',
      50: "50"
    }
    async function checkRegex() {

      let data = regex.value
      if (regex.value === "") {
        data = defaultInput.value
      }

      const config = {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({"count": count.value, "params": [data]})
      }

      const response = await fetch("/backend/regex", config)
          .then(data => data.json()).catch(() => null)

      if (response === null) {
        message.error("后端响应失败！")
        return
      }
      if (response.errorCode !== 0) {
        message.warning("正则表达式解析失败")
        code.value = "正则表达式解析失败"
        return
      }
      code.value = response.data.join("\n");
    }

    async function intoDB() {
      let data = regex.value
      if (regex.value === "") {
        data = defaultInput.value
      }

      const config = {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({"params": data, "rulerType": "regex"})
      }

      const response = await fetch("/backend/registerRegex", config)
          .then(data => data.json()).catch(() => null)

      if (response === null) {
        message.error("后端响应失败！")
        return
      }

      if (response.errorCode === -89) {
        message.warning("规则已经存在")
        return
      }

      if (response.errorCode === 0) {
        message.success("成功注册")
        return
      }

      message.warning("注册规则失败")
    }

    return {
      code,
      regex,
      defaultInput,
      checkRegex,
      count,
      marks,
      intoDB
    }
  }
}
</script>

<style scoped>

</style>