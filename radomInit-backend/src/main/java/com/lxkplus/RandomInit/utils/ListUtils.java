package com.lxkplus.RandomInit.utils;


import java.util.*;

public class ListUtils {
    public static List<List<String>>  rowNormaliser(List<List<String>> list) {
        TreeMap<Integer, List<String>> treeMap = new TreeMap<>();
        for (List<String> stringList : list) {
            for (int i = 0; i < stringList.size(); i++) {
                treeMap.computeIfAbsent(i, k -> new ArrayList<>()).add(stringList.get(i));
            }
        }
        return new ArrayList<>(treeMap.values());
    }
}
