package com.lxkplus.RandomInit.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildRuler {
    @Min(1)
    @Max(1000)
    Integer count;
    /**
     * 规则类型
     */
    String rulerType;
    /**
     * 参数
     */
    List<String> params;
}
