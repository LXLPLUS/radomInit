<template>
  <n-grid :x-gap="12" :cols="5">
    <n-gi :span="2">
      <n-input show-count placeholder="日志信息"/>
    </n-gi>
    <n-gi :span="2">
    </n-gi>
    <n-gi :span="1">
      <n-space vertical>
        <n-gradient-text type="primary">我是张三</n-gradient-text>
        <n-select>添加规则</n-select>
        <n-select>删除规则</n-select>
      </n-space>
    </n-gi>
  </n-grid>
</template>

<script>
export default {
  name: "LogExplain"
}
</script>

<style scoped>

</style>