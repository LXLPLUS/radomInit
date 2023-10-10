<template>
  <n-space>
    <n-input :maxlength="20" placeholder="请输入查询条件" style="width: 140%" v-model:value="filerRuler" @input="filterByRuler"/>
    <n-button type="info" @click="filterByRuler">查询</n-button>
  </n-space>
  <n-data-table :columns="columns" :data="filterData" :pagination="pagination" striped/>
</template>

<script>

import getHook from "../../hooks/getHook";
import { onMounted } from "vue";
import {NButton} from "naive-ui";

export default {
  name: "TableList",
  setup() {
    const columns = [
      {
        title: "表名",
        key: "tableName",
      },
      {
        title: "更新时间",
        key: "updateTime",
      },
      {
        title: "版本数量",
        key: "count",
        width: "60%"
      },
      {
        title: "操作",
        key: "action",
        width: "10%",
        align: "center",
        children: [
          {
            key: "view",
            render(row) {
              return h(
                NButton,
                {
                  size: "small",
                  type: "primary",
                  round: "round",
                  onclick: () => console.log(row)
                },
                {
                  default: () => "查看版本"
                }
              )
            }
          },
          {
            key: "ddl",
            render(row) {
              return h(
                  NButton,
                  {
                    size: "small",
                    type: "primary",
                    round: "round",
                    onclick: () => console.log(row)
                  },
                  {
                    default: () => "DDL"
                  }
              )
            }
          },
          {
            key: "delete",
            render(row) {
              return h(
                  NButton,
                  {
                    size: "small",
                    type: "warning",
                    round: "round",
                    onclick: () => console.log(row)
                  },
                  {
                    default: () => "删除"
                  }
              )
            }
          }
        ]
      }
    ]

    const data = reactive([])
    const filterData = reactive([])
    const filerRuler = ref("")
    const pagination = {
      pageSize: 10
    }

    onMounted(() => updateData())

    async function updateData() {
      const response = await getHook().get("backend/tableList")
      Object.assign(data, response)
      Object.assign(filterData, data)
    }

    function filterByRuler() {
      filterData.length = 0
      for (const row of data) {
        if (row["tableName"] !== void 0 && row["tableName"].indexOf(filerRuler.value) >= 0) {
          filterData.push(row)
        }
      }
    }

    return {
      columns,
      updateData,
      onMounted,
      filerRuler,
      filterByRuler,
      filterData,
      pagination
    }
  }

}
</script>

<style scoped>

</style>