package com.lxkplus.RandomInit.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.model.VO.BuildRuler;
import com.lxkplus.RandomInit.model.VO.SelectOption;
import com.lxkplus.RandomInit.service.RandomService;
import jakarta.annotation.Resource;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Data
@Component
@Scope("prototype")
public class FreeMakerMap implements Map<String, String> {

    List<SelectOption> list = new ArrayList<>();

    @Resource
    RandomService randomService;

    @Resource
    ObjectMapper objectMapper;

    public FreeMakerMap(List<SelectOption> list) {
        this.list.addAll(list);
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return true;
    }

    @Override
    public boolean containsValue(Object value) {
        return true;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            return "null";
        }
        String[] split = key.toString().split("\\|");
        String builderRuler = split[0];
        List<String> params = new ArrayList<>(List.of(split));
        params.remove(0);

        // 先尝试默认的规则是否可用，如果可用那么直接生成
        try {
            Object o = randomService.randomByRuler(builderRuler, params);
            if (o != null) {
                return objectMapper.convertValue(o, String.class);
            }
        } catch (NormalErrorException e) {
            e.printStackTrace();
        }

        // 遍历所有规则，如果有匹配的规则那么按照规则进行生成
        // 现在暂时只有正则表达式的规则
        for (SelectOption option : list) {
            if (Objects.equals(key.toString(), option.getLabel())) {
                try {
                    return randomService.getDataByRegex(new BuildRuler(1, "regex", option.getParams())).get(0);
                } catch (NormalErrorException e) {
                    e.printStackTrace();
                    return ((SelectOption) key).getLabel();
                }
            }
        }
        return key.toString();
    }

    @Nullable
    @Override
    public String put(String key, String value) {
        return null;
    }

    @Override
    public String remove(Object key) {
        return null;
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends String> m) {
        // Not Need
    }

    @Override
    public void clear() {
        // Not Need
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return Collections.emptySet();
    }

    @NotNull
    @Override
    public Collection<String> values() {
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public Set<Entry<String, String>> entrySet() {
        return Collections.emptySet();
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
