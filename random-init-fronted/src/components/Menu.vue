<template>
  <n-layout>
    <Header/>
  </n-layout>
  <n-layout has-sider
            sider-placement="left">
    <n-layout-sider
        bordered
        collapse-mode="width"
        :collapsed-width="64"
        :width="240"
        :collapsed="!collapsed"
        show-trigger
        @collapse="collapsed = true"
        @expand="collapsed = false"
    >
      <n-switch v-model:value="collapsed">
        <template #checked>
          显示
        </template>
        <template #unchecked>
          隐藏
        </template>
      </n-switch>
    <n-menu :options="menuOptions"
            :collapsed-width="64"
            :collapsed-icon-size="22"/>
    </n-layout-sider>
    <n-layout-content style="margin-left: 50px">
      <router-view/>
    </n-layout-content>
  </n-layout>

</template>

<script lang="ts">
import { defineComponent, h, Component, ref } from 'vue'
import { NIcon } from 'naive-ui'
import type { MenuOption } from 'naive-ui'
import { RouterLink } from 'vue-router'
import LoginAndRegister from "./LoginAndRegister.vue"
import {
  LaptopOutline as WorkIcon,
} from '@vicons/ionicons5'
import Header from "./Header.vue";

function renderIcon (icon: Component) {
  return () => h(NIcon, null, { default: () => h(icon) })
}

const menuOptions: MenuOption[] = [
  {
    label: () =>
        h(
            RouterLink,
            {
              to: {
                path: '/LoginAndRegister'
              }
            },
            { default: () => '登录/注册' }
        ),
    key: 'LoginAndRegister',
    icon: renderIcon(WorkIcon)
  },
  {
    label: "数据库/表格/行列",
    key: "database",
    icon: renderIcon(WorkIcon),
    children: [
      {
        label: () =>
            h(
                RouterLink,
                {
                  to: {
                    path: '/tableCreate'
                  }
                },
                { default: () => '新建表格' }
            ),
        key: 'tableCreate',
        icon: renderIcon(WorkIcon)
      }
    ]
  },
  {
    label: " 数据生成",
    key: "dataBuilder",
    icon: renderIcon(WorkIcon),
    children: [
      {
        label: () =>
            h(
                RouterLink,
                {
                  to: {
                    path: '/regex'
                  }
                },
                { default: () => '正则表达式' }
            ),
        key: 'regex',
        icon: renderIcon(WorkIcon)
      },
      {
        label: () =>
            h(
                RouterLink,
                {
                  to: {
                    path: '/enum'
                  }
                },
                { default: () => '状态值枚举' }
            ),
        key: 'enum',
        icon: renderIcon(WorkIcon)
      },
      {
        label: () =>
            h(
                RouterLink,
                {
                  to: {
                    path: '/builderRuler'
                  }
                },
                { default: () => '固定/自定义规则' }
            ),
        key: 'buildRuler',
        icon: renderIcon(WorkIcon)
      }
    ]
  }
]

export default defineComponent({
  components: {
    Header,
    LoginAndRegister
  },
  setup () {
    return {
      activeKey: ref(null),
      collapsed: ref(true),
      menuOptions
    }
  }
})
</script>