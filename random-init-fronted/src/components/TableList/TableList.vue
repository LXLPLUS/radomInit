<template>
  <n-data-table :columns="columns" :data="data">
  </n-data-table>
  <n-button @click="updateData">刷新</n-button>
</template>

<script>

import getHook from "../../hooks/getHook";
import { onMounted } from "vue";

export default {
  name: "TableList",
  setup() {
    const columns = [
      {
        title: "表名",
        key: "tableName"
      },
      {
        title: "时间",
        key: "updateTime"
      },
      {
        title: "版本数量",
        key: "count"
      }
    ]

    const data = reactive([])

    onMounted(() => updateData())

    async function updateData() {
      const response = await getHook().get("backend/tableList")
      Object.assign(data, response)
    }

    return {
      columns,
      updateData,
      data,
      onMounted
    }
  }

}
</script>

<style scoped>

</style>