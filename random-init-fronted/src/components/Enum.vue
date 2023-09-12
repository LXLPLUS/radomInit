<template>
  <n-dynamic-input v-model:value="value" placeholder="请尝试添加枚举值" :max="100">

  </n-dynamic-input>
</template>

<script>
export default {
  name: "Enum",
  setup() {
    let value = ref([])
    return {
      value
    }
  }
}
</script>

<style scoped>

</style>