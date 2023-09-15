<template>
  <n-grid x-gap="12" :cols="7">
    <n-gi>
      <n-input placeholder="表名" v-model:value="tableHead.tableName"></n-input>
    </n-gi>
    <n-gi>
      <n-input placeholder="引擎" v-model:value="tableHead.engine"></n-input>
    </n-gi>
    <n-gi>
      <n-input placeholder="默认编码" v-model:value="tableHead.charset"></n-input>
    </n-gi>
    <n-gi>
      <n-input placeholder="字符集" v-model:value="tableHead.shotRuler"></n-input>
    </n-gi>
    <n-gi>
      <n-input placeholder="注释" v-model:value="tableHead.comment"></n-input>
    </n-gi>
  </n-grid>

  <n-divider/>

  <n-dynamic-input :on-create="onCreate"
                   v-model:value="columns"
                   :min="1"
                   :max="100"
                   show-sort-button>
    <template #default="{ value }">
      <n-grid x-gap="12" :cols="12">
        <n-gi :span="2">
          <n-input placeholder="字段名" v-model:value="value.rowName"></n-input>
        </n-gi>
        <n-gi :span="2">
          <n-mention placeholder="类型" v-model:value="value.dataType"></n-mention>
        </n-gi>
        <n-gi>
          <n-switch v-model:value="value.pri" size="small">
            <template #checked>
              主键
            </template>
            <template #unchecked>
              非主键
            </template>
          </n-switch>
        </n-gi>
        <n-gi>
          <n-switch v-model:value="value.allowNull" size="small" :rail-style="nullStyle">
            <template #checked>
              允许null
            </template>
            <template #unchecked>
              非null
            </template>
          </n-switch>
        </n-gi>
        <n-gi :span="2">
          <n-input placeholder="默认值" v-model:value="value.default"></n-input>
        </n-gi>
        <n-gi :span="2">
          <n-select placeholder="额外信息" v-model:value="value.extra" :options="extraParams"></n-select>
        </n-gi>
        <n-gi :span="2">
          <n-input placeholder="注释" v-model:value="value.comment"></n-input>
        </n-gi>
      </n-grid>
    </template>
  </n-dynamic-input>

  <n-divider/>

  <n-dynamic-input :on-create="onCreateIndex" v-model:value="tableIndex">
    <template #default="{ value }">
      <n-grid x-gap="12" :cols="6">
        <n-gi>
          <n-input v-model:value="value.indexName" placeholder="索引名称"></n-input>
        </n-gi>
        <n-gi>
          <n-select v-model:value="value.indexType" placeholder="索引类型"></n-select>
        </n-gi>
        <n-gi>
          <n-select multiple v-model:value="value.indexColumns"></n-select>
        </n-gi>
      </n-grid>
    </template>
  </n-dynamic-input>

  <br/>
  <br/>
  <n-grid :cols="4">
    <n-gi>
    <n-button type="warning" @click="clearData">
      清空
    </n-button>
  </n-gi>
    <n-gi>
      <n-button type="success" @click="gatherTable">
        生成
      </n-button>
    </n-gi>
  </n-grid>


</template>

<script>
import {defineComponent} from "vue";

export default defineComponent({
  name: "tableCreate",
  setup() {

    const extraParams = [
      {
        label: "",
        value: ""
      },
      {
        label: "AUTO_INCREMENT",
        value: "AUTO_INCREMENT"
      },
      {
        label: "ON UPDATE CURRENT_TIMESTAMP",
        value: "ON UPDATE CURRENT_TIMESTAMP"
      }
    ]

    let tableHead = reactive({
      tableName: "",
      engine: "InnoDB",
      charset: "utf8mb4",
      shotRuler: "utf8mb4_0900_ai_ci",
      comment: ""
    })

    let tableIndex = ref(
        [
          {
            indexName: "pk_id",
            indexType: "",
            indexColumns: ["id"]
          }
        ]
    )

    let nullStyle = ({ focused, checked }) => {
        const style = {background: "#FF9900"};
        if (checked) {
          style.background = "green"
        }
        return style
      }

    let columns = ref(
        [
          {
            rowName: "id",
            dataType: "int",
            pri: true,
            allowNull: false,
            default: null,
            extra: "AUTO_INCREMENT",
            comment: "id"
          },
          {
            rowName: "createTime",
            dataType: "datetime",
            pri: false,
            allowNull: false,
            default: null,
            extra: null,
            comment: null
          },
          {
            rowName: "updateTime",
            dataType: "datetime",
            pri: false,
            allowNull: false,
            default: null,
            extra: null,
            comment: null
          },
          {
            rowName: "isDelete",
            dataType: "datetime",
            pri: false,
            allowNull: false,
            default: "false",
            extra: null,
            comment: null
          }
        ]
    )

    const defaultColumn = reactive({
      rowName: "",
      dataType: "int",
      pri: false,
      allowNull: true,
      default: null,
      extra: null,
      comment: null
    })

    function onCreateIndex() {
      return {
        indexName: "",
        indexColumns: []
      }
    }

    function clearData() {
      columns.value =
          [
              {
                rowName: "id",
                dataType: "int",
                pri: true,
                allowNull: false,
                default: null,
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
                shotRuler: "utf8mb4_0900_ai_ci",
                comment: ""
              }
          )
      )

      tableIndex.value =
        [
          {
            indexName: "pk_id",
            indexType: "",
            indexColumns: ["id"]
          }
        ]
    }

    function onCreate() {
      return Object.assign({}, defaultColumn)
    }

    function gatherTable() {
      console.log(columns)
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
      extraParams,
      nullStyle
    }
  }
})
</script>