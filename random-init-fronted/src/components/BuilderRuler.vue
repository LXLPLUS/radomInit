  <template>
  <n-grid x-gap="12" :cols="2">
    <n-gi>
      <n-gradient-text type="info">
        固定规则
      </n-gradient-text>
      <n-form label-align="left" :model="modelRef" label-placement="left" label-width="auto" >
        <n-form-item label="固定规则">
          <n-select v-model:value="modelRef.ruler"
                    size="large"
                    :options="options"
                    @update:value="changeInput" style="width: 40vw"
          />
        </n-form-item>
        <n-form-item label=" 参数1">
          <n-input v-model:value="modelRef.param1"
                   :placeholder="modelRef.comment1"
                   :disabled="modelRef.param1disabled"
                   style="width: 40vw"/>
        </n-form-item>
        <n-form-item label=" 参数2">
          <n-input v-model:value="modelRef.param2"
                   :placeholder="modelRef.comment2"
                   :disabled="modelRef.param2disabled"
                   style="width: 40vw"/>
        </n-form-item>
        <n-form-item>
          <n-slider v-model:value="modelRef.count" :marks="modelRef.marks" :max="50"/>
        </n-form-item>
        <n-button type="primary" @click="generate">
          生成
        </n-button>
      </n-form>

      <br/>
      <br/>
      <n-divider />
      <br/>
      <br/>


      <n-gradient-text type="info">
        自定义规则
      </n-gradient-text>
      <n-form label-align="left" :model="modelRef2" label-placement="left" label-width="auto" >
        <n-form-item label="自定义规则">
          <n-select v-model:value="modelRef2.ruler"
                    label-field="label"
                    value-field="label"
                    size="large"
                    :options="modelRef2.options"
                    style="width: 40vw"
          />
        </n-form-item>
        <n-form-item>
          <n-slider v-model:value="modelRef2.count" :marks="modelRef2.marks" :max="50"/>
        </n-form-item>
        <n-space>
          <n-button type="primary" @click="generate2">
            生成
          </n-button>
          <n-button type="primary" @click="updateOptions">
            刷新
          </n-button>
        </n-space>
      </n-form>

    </n-gi>
    <n-gi>
      <Code v-model:code="code"></Code>
    </n-gi>
  </n-grid>
</template>

<script>
import { onBeforeMount } from "vue";
import Code from "./Code.vue"
import {useMessage} from "naive-ui";

export default {
  name: "BuilderRuler",
  components: {
    Code
  },
  setup() {

    const message = useMessage()

    let code = ref("")

    const marks = {
      10: '10',
      20: '20',
      30: '30',
      40: '40',
      50: "50"
    }

    const options = [
      {
        label:"随机字母",
        value:"string"
      },
      {
        label: "随机字母|最大长度",
        value: "string|param1"
      },
      {
        label: "随机字母|最小长度|最大长度",
        value: "string|param1|param2"
      },
      {
        label: "数字(int/long)",
        value: "range"
      },
      {
        label: "数字|最小值",
        value: "range|param1"
      },
      {
        label: "数字|最小值|最大值",
        value: "range|param1|param2"
      },
      {
        label: "时间",
        value: "time"
      },
      {
        label: "时间|开始时间",
        value: "time|param1"
      },
      {
        label: "时间|开始时间|结束时间",
        value: "time|param1|param2"
      },
      {
        label: "手机号",
        value: "手机号"
      },
      {
        label: "邮箱",
        value: "邮箱"
      },
      {
        label: "名称",
        value: "name"
      },
      {
        label: "uuid",
        value: "uuid"
      }
    ]

    let modelRef = reactive({
      ruler : "string",
      param1 : "",
      param2 : "",
      param1disabled : true,
      param2disabled : true,
      comment1 : "",
      comment2 : "",
      count : 10,
      marks : marks,
      options: options,
    })

    let modelRef2 = reactive({
      ruler : "",
      count : 10,
      options: [],
      marks: marks,
    })

    async function generate2() {
      code.value = ""

      let row;
      let params;
      let rulerType;
      for (row of modelRef2.options) {
        if (row.label === modelRef2.ruler) {
          params = row.params
          rulerType = row.rulerType
        }
      }

      const config = {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({"count": modelRef.count, "rulerType": rulerType, "params": params})
      }

      const response = await fetch("/backend/builderRuler", config).then(data => data.json()).catch((e) => console.log(e))

      if (response === null) {
        message.error("后端响应失败！")
        return
      }

      if (response.errorCode !== 0) {
        message.warning("参数错误！")
        return
      }

      code.value = response.data.join("\n");
    }

    async function generate() {

      code.value = ""

      let params = []
      if (modelRef.param1 !== "") {
        params.push(modelRef.param1)
      }
      if (modelRef.param2 !== "") {
        params.push(modelRef.param2)
      }

      const config = {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({"count": modelRef.count, "rulerType": modelRef.ruler, "params": params})
      }
      const response = await fetch("/backend/builderRuler", config).then(data => data.json()).catch(() => null)

      if (response === null) {
        message.error("后端响应失败！")
        return
      }

      if (response.errorCode !== 0) {
        message.warning("参数错误！")
        return
      }

      code.value = response.data.join("\n");
    }


    async function updateOptions() {
      modelRef2.options = await fetch("/backend/userRuler").then(data => data.json()).then(data => data.data)
          .catch(() => [])

    }

    onBeforeMount(() => updateOptions())

    function changeInput() {
      modelRef.param1disabled = modelRef.ruler.indexOf("param1") < 0
      modelRef.param2disabled = modelRef.ruler.indexOf("param2") < 0
      modelRef.comment1 = "暂不可用"
      modelRef.comment2 = "暂不可用"
      modelRef.param1 = ""
      modelRef.param2 = ""

      if (modelRef.ruler === "string|param1") {
        modelRef.comment1 = "字符串最长长度(数字)"
      }
      else if (modelRef.ruler === "string|param1|param2") {
        modelRef.comment1 = "字符串最短长度(数字)"
        modelRef.comment2 = "字符串最长长度(数字)"
      }
      else if (modelRef.ruler === "range|param1") {
        modelRef.comment1 = "最小数字"
      }
      else if (modelRef.ruler === "range|param1|param2") {
        modelRef.comment1 = "最小数字"
        modelRef.comment2 = "最大数字"
      }
      else if (modelRef.ruler === "time|param1") {
        modelRef.comment1 = "开始时间(yyyy-MM-DD HH:mm:ss)"
      }
      else if (modelRef.ruler === "time|param1|param2") {
        modelRef.comment1 = "开始时间(yyyy-MM-DD HH:mm:ss)"
        modelRef.comment2 = "结束时间(yyyy-MM-DD HH:mm:ss)"
      }
    }

    return {
      options,
      changeInput,
      modelRef,
      modelRef2,
      code,
      marks,
      generate,
      generate2,
      updateOptions
    }
  }
}
</script>