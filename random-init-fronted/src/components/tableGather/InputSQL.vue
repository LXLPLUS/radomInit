<template>
  <n-modal v-model:show="inputSQLParams.condition" title="导入" :bordered="true" style="background-color: white; border: 1vw greenyellow">
    <div>
      <n-gradient-text type="success" size="30" style="margin-left: 3vw; margin-top: 1.5vw">导入建表 Create</n-gradient-text>
      <n-divider></n-divider>
      <n-grid x-gap="20" cols="20" style="width: 75vh; position: center">
        <n-gi></n-gi>
        <n-gi :span="18">
          <n-input
              type="textarea"
              placeholder="请输入建表语句"
              :autosize="inputSize"
              :clearable="true"
              :show-count="true"
              :maxlength="10000"
              v-model:value="props.createSql.data"
          />
        </n-gi>
        <n-gi></n-gi>
        <n-gi :span="20">
          <br/>
        </n-gi>
        <n-gi :span="2">
          <br/>
        </n-gi>
        <n-gi span="5">
          <n-button type="success"
                    style="text-align: center;width: 75%"
                    @click="gather">
            提交
          </n-button>
        </n-gi>
        <n-gi span="5">
          <n-button type="error"
                    style="text-align: center"
                    @click="close">
            退出
          </n-button>
        </n-gi>
      </n-grid>
      <br>
    </div>
  </n-modal>

</template>

<script>
export default {
  name: "InputSQL",
  props: ["inputSQLParams", "createSql"],
  emits: ["gather"],
  setup(props, context) {
    const inputSize = {
      "minRows": 15,
      "maxRows": 15
    }

    function close() {
      props.inputSQLParams.condition = false
    }

    function gather() {
      context.emit("gather")
    }
    return {
      inputSize,
      props,
      close,
      gather
    }
  }
}

</script>

<style scoped>

</style>