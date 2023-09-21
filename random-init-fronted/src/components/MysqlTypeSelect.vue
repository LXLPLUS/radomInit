<template>
  <n-grid :cols="24" :x-gap="4" :y-gap="4">
    <n-gi :span="3">
      <n-input placeholder="字段名" v-model:value="value.rowName"></n-input>
    </n-gi>
    <n-gi :span="3">
      <n-select placeholder="类型"
                :options="typeOptions"
                v-model:value="props.value.dataType"
                :show-checkmark="true"
                tag
                filterable
                @update:value="updateDefault"/>
    </n-gi>
    <n-gi :span="1">
      <n-input-number :min="0" :show-button="false"
                      v-model:value="props.value.param1"
                      v-model:disabled="conditions.param1disabled"
                      placeholder=""/>
    </n-gi>
    <n-gi :span="1">
      <n-input-number :min="0" :show-button="false"
                      v-model:value="props.value.param2"
                      v-model:disabled="conditions.param2disabled"
                      placeholder=""/>
    </n-gi>
    <n-gi :span="2">
      <n-switch v-model:value="value.pri" size="medium" @update:value="clearPri">
        <template #checked>
          主键
        </template>
        <template #unchecked>
          非主键
        </template>
      </n-switch>
    </n-gi>
    <n-gi :span="2">
      <n-switch v-model:value="value.allowNull" size="medium" :rail-style="nullStyle">
        <template #checked>
          允许null
        </template>
        <template #unchecked>
          非null
        </template>
      </n-switch>
    </n-gi>
    <n-gi :span="4">
      <n-input placeholder="默认值" v-model:value="value.defaultMessage"></n-input>
    </n-gi>
    <n-gi :span="4">
      <n-select placeholder="额外信息" v-model:value="value.extra" :options="extraParams"></n-select>
    </n-gi>
    <n-gi :span="4">
      <n-input placeholder="注释" v-model:value="value.comment"></n-input>
    </n-gi>
  </n-grid>

</template>

<script>
import mysqlParams from "../hooks/mysqlParams";
export default {
  name: "MysqlTypeSelect",
  props: ["value"],
  emits: ["changeIndex"],
  setup(props, context) {

    const typeOptions  = mysqlParams().typeOptions

    let conditions = reactive({
      param1disabled : true,
      param2disabled : true
    })

    onBeforeMount(() => {
      conditions.param1disabled = true
      conditions.param2disabled = true

      if (props.value.dataType.endsWith("CHAR")) {
        conditions.param1disabled = false
      }
      else if (props.value.dataType.endsWith("INT") || props.value.dataType.startsWith("INT")) {
        conditions.param1disabled = false
      }
      else if (props.value.dataType === "DECIMAL" || props.value.dataType === "FLOAT" || props.value.dataType === "DOUBLE") {
        conditions.param1disabled = false
        conditions.param2disabled = false
      }
    })

    function clearPri() {
      context.emit("changeIndex", props.value)
    }

    function updateDefault() {

      conditions.param1disabled = true
      conditions.param2disabled = true

      props.value.param1 = null
      props.value.param2 = null

      props.value.default = ""
      props.value.allowNull = true

      if (props.value.dataType.endsWith("CHAR")) {
        conditions.param1disabled = false
        props.value.param1 = 255
        props.value.defaultMessage = ""
      }
      else if (props.value.dataType.endsWith("INT") || props.value.dataType.startsWith("INT")) {
        conditions.param1disabled = false
        props.value.param1 = 10
        props.value.defaultMessage = ""
      }
      else if (props.value.dataType === "DECIMAL" || props.value.dataType === "FLOAT" || props.value.dataType === "DOUBLE") {
        conditions.param1disabled = false
        props.value.param1 = 10
        conditions.param2disabled = false
        props.value.param2 = 2
      }
      else if (props.value.dataType === "DATETIME") {
        props.value.allowNull = false
        props.value.defaultMessage = "CURRENT_TIMESTAMP"
      }
      else if (props.value.dataType === "BOOLEAN") {
        props.value.defaultMessage = "0"
        props.value.allowNull = false
      }
    }

    let nullStyle = ({_, checked}) => {
      const style = {background: "#FF9900"};
      if (checked) {
        style.background = "green"
      }
      return style
    }

    const extraParams = mysqlParams().extraParams

    return {
      typeOptions,
      updateDefault,
      conditions,
      props,
      nullStyle,
      extraParams,
      clearPri
    }
  }
}
</script>

<style scoped>

</style>