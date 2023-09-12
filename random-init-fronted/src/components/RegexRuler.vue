<template>
  <br>
  <br>
  <h3>have a try？</h3>
  <div>
    点击下验证,看右侧是否出现了随机的数据？ 复制到需要的地方即可。<br>
    点击入库，可以在数据库定义的时候绑定该随机树数
  </div>
  <h3>数字</h3>
  <ul>
    <li>数字：[0-9]*</li>
    <li>n位的数字：\d{n}</li>
    <li>m-n位的数字：\d{m,n}</li>
    <li>有1~3位小数的正实数：[0-9]+(\.[0-9]{1,3})?</li>
    <li>浮点数：(-?\d+)(\.\d+)? 或 -?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)</li>
    <li>任意一个不以0开头的数字：(0|[1-9][0-9]*)</li>
  </ul>
  <h3>特殊</h3>
  <ul>
    <li>中文字符的正则表达式：[\u4e00-\u9fa5]</li>
    <li>英文和数字：[A-Za-z0-9]+$ 或 ^[A-Za-z0-9]{4,40}</li>
    <li>禁止输入含有~的字符：[^~]+</li>
  </ul>
</template>

<script>
export default {
  name: "RegexRuler"
}
</script>

<style scoped>

</style>