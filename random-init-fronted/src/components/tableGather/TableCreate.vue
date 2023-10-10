<template>
  <n-grid x-gap="12" :cols="15">
    <n-gi :span="2">
      <n-input placeholder="表名"
               v-model:value="tableHead.tableName"
      />
    </n-gi>
    <n-gi :span="2">
      <n-select placeholder="引擎"
                v-model:value="tableHead.engine"
                tag
                filterable
                :options="engineParams"
      />
    </n-gi>
    <n-gi :span="2">
      <n-select placeholder="默认编码"
                v-model:value="tableHead.charset"
                :options="charSetParams"
                label-field="value"
                value-field="value"/>
    </n-gi>
    <n-gi :span="3">
      <n-select placeholder="字符集"
                v-model:value="tableHead.sortRuler"
                :options="collationParams"
                style="font-size: 50%"/>
    </n-gi>
    <n-gi :span="2">
      <n-input placeholder="注释"
               v-model:value="tableHead.comment"/>
    </n-gi>
    <n-gi :span="2">
      <n-switch v-model:value="tableHead.ifNotExist" size="medium">
        <template #checked>
          if Not Exist
        </template>
        <template #unchecked>
          默认
        </template>
      </n-switch>
    </n-gi>
  </n-grid>

  <n-divider/>

  <n-dynamic-input :on-create="onCreate"
                   v-model:value="columns"
                   :min="1"
                   :max="200"
                   show-sort-button>
    <template #default="{ value }">
      <MysqlTypeSelect :value="value"
                       @changeIndex="changeIndex"/>
    </template>
  </n-dynamic-input>

  <n-divider/>

  <n-dynamic-input
      :on-create="onCreateIndex"
      v-model:value="tableIndex"
  >
    <template #default="{ value, index }">
      <n-grid x-gap="12" :cols="6">
        <n-gi>
          <n-input v-model:value="value.indexName" placeholder="索引名称"/>
        </n-gi>
        <n-gi>
          <n-select v-model:value="value.indexType"
                    placeholder="索引类型"
                    :options="indexParams"
          />
        </n-gi>
        <n-gi :span="2">
          <n-select multiple v-model:value="value.indexColumns" :options="columnsLabel"/>
        </n-gi>
      </n-grid>
    </template>
  </n-dynamic-input>

  <br/>
  <br/>
  <n-grid :cols="8">
    <n-gi>
      <n-button type="warning" @click="clearData">
        清空
      </n-button>
    </n-gi>
    <n-gi>
      <n-button type="success" @click="inputSQL">
        文本导入
      </n-button>
    </n-gi>
    <n-gi>
      <n-button type="success" @click="gatherTable">
        sql生成
      </n-button>
    </n-gi>
    <n-gi>
      <n-button type="success" @click="testAndSave"> 记录</n-button>
    </n-gi>
  </n-grid>

  <br/>
  <br/>
  <n-grid :cols="4" :x-gap="12">
    <n-gi>
      <n-popselect v-model:value="mybatisConfigs" trigger="click" :options="mybatisSelectParams" multiple>
        <n-button type="primary">修改mybatis数据格式</n-button>
      </n-popselect>
    </n-gi>
    <n-gi>
      <n-button type="success" @click="gatherMybatis">生成mybatis代码({{ mybatisConfigs.length }})</n-button>
    </n-gi>
  </n-grid>


  <n-divider/>

  <div style="margin-right: 5%">
    <Code v-model:code="code"></Code>
  </div>


  <InputSQL v-model:inputSQLParams="inputSQLParams"
            v-model:createSql="createSql"
            @gather="gather"
  />

</template>

<script>
import {defineComponent} from "vue";
import Code from "../Code.vue";
import MysqlTypeSelect from "../MysqlTypeSelect.vue";
import InputSQL from "./InputSQL.vue";
import mysqlParams from "../../hooks/mysqlParams";
import getHook from "../../hooks/getHook";
import postMapper from "../../hooks/postMapper";
import {
  ChevronDoubleUp16Regular as upIcon,
  ChevronDoubleDown16Filled as downIcon,
} from '@vicons/fluent'

import {
  AddBoxOutlined as addIcon,
  IndeterminateCheckBoxOutlined as removeIcon
} from "@vicons/material"
import messageHook from "../../hooks/messageHook";

export default defineComponent({
  name: "tableCreate",
  components: {
    InputSQL,
    MysqlTypeSelect,
    Code,
    upIcon,
    downIcon,
    addIcon,
    removeIcon
  },
  setup() {

    const code = ref("")
    const inputSQLParams = reactive({
      "condition": false
    })

    const message = messageHook().message
    const dialog = messageHook().dialog

    const charSetParams = mysqlParams().charSetParams
    const indexParams = mysqlParams().indexParams
    const engineParams = mysqlParams().engineParams
    const collationParams = mysqlParams().collationParams
    const tableHead = mysqlParams().tableHead
    const tableIndex = mysqlParams().tableIndex
    const columns = mysqlParams().columns


    const createSql = reactive({
      data: ""
    })

    const mybatisSelectParams = reactive([
      {
        label: "强制转化为字符串",
        value: "allString"
      },
      {
        label: "使用默认表格名",
        value: "defaultTableName"
      },
      {
        label: "带注释",
        value: "addComment"
      },
      {
        label: "非空校验",
        value: "valid"
      },
      {
        label: "使用包装类",
        value: "boxed"
      }
    ])

    const mybatisConfigs = ref(
        [
          "defaultTableName",
          "addComment",
          "valid",
          "boxed"
        ]
    )

    let columnsLabel = computed(() => {
          const columnsList = []
          for (const column of columns.value) {
            columnsList.push({"label": column.rowName + "(正序)", "value": column.rowName + " asc"})
          }
          for (const column of columns.value) {
            columnsList.push({"label": column.rowName + "(倒序)", "value": column.rowName + " desc"})
          }
          return columnsList
        }
    )

    function onCreateIndex() {
      return {
        indexName: "",
        indexType: "KEY",
        indexColumns: []
      }
    }

    function clearData() {
      columns.value =
          [
            {
              rowName: "id",
              dataType: "INT",
              pri: true,
              allowNull: false,
              defaultMessage: null,
              extra: "AUTO_INCREMENT",
              comment: "id"
            }
          ]

      Object.assign(tableHead,
          reactive(
              {
                tableName: "",
                engine: "InnoDB",
                charset: "utf8mb4",
                sortRuler: "utf8mb4_0900_ai_ci",
                comment: "",
                ifNotExist: true
              }
          )
      )

      tableIndex.value =
          [
            {
              indexName: "pk_id",
              indexType: "PRIMARY KEY",
              indexColumns: ["id asc"]
            }
          ]
      code.value = ""
    }

    function onCreate() {
      return Object.assign({}, mysqlParams().defaultColumn)
    }

    function inputSQL() {
      inputSQLParams.condition = true
    }

    async function gatherTable() {
      const response = await postMapper().post("/backend/gatherSql", {
        "tableHeader": tableHead,
        "tableColumns": columns.value,
        "tableIndices": tableIndex.value
      })

      code.value = response["sql"]
    }

    async function gather() {

      const response = await postMapper().post("/backend/explainSql", {
        "sql": createSql.data
      })

      if (Object.keys(response).length > 0) {
        Object.assign(tableHead, response.tableHeader)
        columns.value = response.tableColumns
        indexParams.value = response.tableIndices
        inputSQLParams.condition = false
        await gatherTable()
      }
    }

    function changeIndex(value) {
      for (const column of columns.value) {
        if (column !== value) {
          column.pri = false
        }
      }

      tableIndex.value = tableIndex.value.filter(function (item) {
        return item.indexType !== "PRIMARY KEY"
      })

      if (value.pri === true) {
        tableIndex.value.unshift({
              indexName: "pk_" + value.rowName,
              indexType: "PRIMARY KEY",
              indexColumns: [value.rowName + " asc"]
            }
        )
      }
    }

    async function gatherMybatis() {
      const config = {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          "tableHeader": tableHead,
          "tableColumns": columns.value,
          "tableIndices": tableIndex.value
        })
      }

      let data = {
        boxed: false,
        valid: false,
        allString: false,
        defaultTableName: false,
        addComment: false
      }

      for (const param of mybatisConfigs.value) {
        data[param] = true
      }

      const url = "/backend/mybatisCode" + getHook().params(data)
      const response = await fetch(url, config)
          .then(data => data.json())

      if (response.data === void 0) {
        message.warning(response["errorMessage"])
        code.value = ""
      }
      else {
        code.value = response.data["code"]
      }
    }

    async function testAndSave() {
      await gatherTable()
      const response = await postMapper().post("/backend/registerDDL", {sql: code.value})
      if (response.pass === true) {
        dialog.success({
          title: "成功入库！"
        })
      }
      else {
        dialog.warning({
          title: response.message
        })
      }
    }

    return {
      onCreate,
      columns,
      gatherTable,
      tableHead,
      onCreateIndex,
      tableIndex,
      clearData,
      code,
      indexParams,
      columnsLabel,
      inputSQLParams,
      inputSQL,
      engineParams,
      charSetParams,
      collationParams,
      gather,
      createSql,
      changeIndex,
      gatherMybatis,
      mybatisConfigs,
      mybatisSelectParams,
      testAndSave
    }
  }
})
</script>