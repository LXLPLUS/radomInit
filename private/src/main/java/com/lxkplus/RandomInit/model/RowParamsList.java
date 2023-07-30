package com.lxkplus.RandomInit.model;

import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.enums.HandlerEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.utils.StringUtils;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RowParamsList {
    List<Map<String, Object>> paramsList;
    String rowName = "rowName";
    String count = "count";
    String builderRuler = "builderRuler";
    int maxLen = 10000;

    /**
     * 检查是否参数足够，如果不足的话就抛出异常
     * @param params 参数表
     * @throws NormalErrorException 参数不符合就会抛出异常
     */
    void addColumn(Map<String, Object> params) throws NormalErrorException {
        if (!params.containsKey(rowName)) {
            throw new NormalErrorException(ErrorEnum.NotEnoughParams, StringUtils.format("缺乏{}参数", rowName));
        }
        if (!params.containsKey(count)) {
            throw new NormalErrorException(ErrorEnum.NotEnoughParams, StringUtils.format("缺乏{}参数", count));
        }

        if ((int) params.get(count) <= 0) {
            throw new NormalErrorException(ErrorEnum.LengthNotEnough,
                    StringUtils.format("获取的最大长度为{}, 小于等于0", count));
        }

        if ((int) params.get(count) >= maxLen) {
            throw new NormalErrorException(ErrorEnum.LengthTooLong);
        }

        if (params.get(builderRuler).equals(HandlerEnum.regex.name())
                && params.getOrDefault("regex", null) == null) {
            throw new NormalErrorException(ErrorEnum.NotEnoughParams);
        }
        paramsList.add(params);
    }
}
