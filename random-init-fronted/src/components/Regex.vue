<template>
  <n-grid x-gap="12" :cols="2">
    <n-gi>
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
      <n-button strong secondary style="text-align: right" type="info" @click="copy">
        复制到剪贴板
      </n-button>
      <br>
        <n-card style="min-height: 800px" :embedded="true">
          <n-code :code="code" language="javascript" show-line-numbers/>
        </n-card>
    </n-gi>
  </n-grid>
</template>

<script lang="ts">
import {ref} from "vue"
import { useMessage } from "naive-ui";
import clipboard3 from 'vue-clipboard3';
import RegexRuler from "./RegexRuler.vue"

export default {
  name: "Regex",
  components: {RegexRuler},
  setup() {
    const message = useMessage()
    let defaultInput = ref("[0-9]{1,12}")
    let code = ref("12312")
    let regex = ref("")
    let count = ref(10)

    const marks = {
      1: '1',
      10: '10',
      20: '20',
      50: '50'
    }

    const { toClipboard } = clipboard3();

    async function copy() {
      try {
        await toClipboard(code.value)
        message.info("复制成功")
      } catch (Error) {
        message.warning("复制失败")
      }
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
      copy,
      intoDB
    }
  }
}
</script>

<style scoped>

</style>