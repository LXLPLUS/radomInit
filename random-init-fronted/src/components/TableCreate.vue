<template>
  <n-grid x-gap="12" :cols="15">
    <n-gi  :span="2">
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
    <n-gi  :span="2">
      <n-select placeholder="默认编码" v-model:value="tableHead.charset" :options="charSetParams" label-field="value" value-field="value"/>
    </n-gi>
    <n-gi  :span="3">
      <n-select placeholder="字符集" v-model:value="tableHead.sortRuler" :options="collationParams" style="font-size: 50%"/>
    </n-gi>
    <n-gi  :span="2">
      <n-input placeholder="注释" v-model:value="tableHead.comment"/>
    </n-gi>
    <n-gi  :span="2">
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

  <n-dynamic-input :on-create="onCreateIndex" v-model:value="tableIndex">
    <template #default="{ value }">
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
        从表单生成sql
      </n-button>
    </n-gi>
    <n-gi>
      <n-button type="success"> 记录 </n-button>
    </n-gi>
  </n-grid>

  <n-divider/>

  <Code v-model:code="code"></Code>

  <InputSQL v-model:inputSQLParams="inputSQLParams"
            v-model:createSql="createSql"
            @gather="gather"
  />


</template>

<script>
import {defineComponent} from "vue";
import Code from "./Code.vue";
import { useMessage } from "naive-ui"
import MysqlTypeSelect from "./MysqlTypeSelect.vue";
import InputSQL from "./InputSQL.vue";
import mysqlParams from "../hooks/mysqlParams";

export default defineComponent({
  name: "tableCreate",
  components: {InputSQL, MysqlTypeSelect, Code},
  setup: function () {

    const code = ref("")
    const inputSQLParams = reactive({
      "condition": false
    })

    const message = useMessage()

    let charSetParams = mysqlParams().charSetParams
    const indexParams = mysqlParams().indexParams
    const engineParams = mysqlParams().engineParams
    const collationParams = mysqlParams().collationParams
    const createSql = reactive({
      data: ""
    })

    let tableHead = reactive({
      tableName: "",
      engine: "InnoDB",
      charset: "utf8mb4",
      sortRuler: "utf8mb4_0900_ai_ci",
      comment: "",
      ifNotExist: true
    })

    let tableIndex = ref(
        [
          {
            indexName: "pk_id",
            indexType: "PRIMARY KEY",
            indexColumns: ["id asc"]
          }
        ]
    )

    let columns = ref(
        [
          {
            rowName: "id",
            dataType: "INT",
            pri: true,
            param1: 10,
            allowNull: false,
            defaultMessage: null,
            extra: "AUTO_INCREMENT",
            comment: "id"
          },
          {
            rowName: "createTime",
            dataType: "DATETIME",
            pri: false,
            allowNull: false,
            defaultMessage: null,
            extra: null,
            comment: null
          },
          {
            rowName: "updateTime",
            dataType: "DATETIME",
            pri: false,
            allowNull: false,
            defaultMessage: null,
            extra: null,
            comment: null
          },
          {
            rowName: "isDelete",
            dataType: "BOOLEAN",
            pri: false,
            allowNull: false,
            defaultMessage: "0",
            extra: null,
            comment: null
          }
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

    const defaultColumn = reactive({
      rowName: "",
      dataType: "VARCHAR",
      param1: 255,
      pri: false,
      allowNull: false,
      defaultMessage: null,
      extra: null,
      comment: null
    })

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
      return Object.assign({}, defaultColumn)
    }

    function inputSQL() {
      inputSQLParams.condition = true
    }

    async function gatherTable() {
      const config = {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({"tableHeader": tableHead, "tableColumns": columns.value, "tableIndices": tableIndex.value})
      }

      const response = await fetch("/backend/gatherSql", config)
          .then(data => data.json())

      code.value = response.data["sql"]
    }

    async function gather() {

      const config = {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({"sql": createSql.data})
      }
      const response = await fetch("/backend/explainSql", config).then(data => data.json())

      if (response["errorCode"] !== 0) {
        message.warning(response["errorMessage"])
      }
      else {
        Object.assign(tableHead, response.data.tableHeader)
        columns.value = response.data.tableColumns
        indexParams.value = response.data.tableIndices
      }

      inputSQLParams.condition = false
      await gatherTable()
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

    return {
      onCreate,
      columns,
      gatherTable,
      defaultColumn,
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
      changeIndex
    }
  }
})
</script>