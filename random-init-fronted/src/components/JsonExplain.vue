<template>
  <n-grid :cols="3">
    <n-gi :span="2">
      <n-input v-model:value="template.input" type="textarea" style="height: 40vh"/>
      <n-input v-model:value="template.output" type="textarea" style="height: 40vh"/>
    </n-gi>
    <n-gi>
      <n-dynamic-input v-model:value="keyList" :on-create="createKey">
        <template #default="{ value }">
          <n-input v-model:value="value.key"/>
          <n-select v-model:value="value.value"/>
        </template>

      </n-dynamic-input>
      <n-button @click="fillTemplate">生成</n-button>
    </n-gi>
  </n-grid>
</template>

<script>
import postMapper from "../hooks/postMapper";

export default {
  name: "jsonExplain",
  setup() {
    const template = reactive(
        {
          input: "",
          output: ""
        }
    )

    const keyList = ref([])

    function createKey() {
      return {
        key: "",
        value: ""
      }
    }

    async function fillTemplate() {
      template.output = await postMapper().post("/backend/templateParamsGather",
          {
            template: template.input
          }
      )
    }

    return {
      template,
      fillTemplate,
      keyList,
      createKey
    }
  }
}
</script>

<style scoped>

</style>