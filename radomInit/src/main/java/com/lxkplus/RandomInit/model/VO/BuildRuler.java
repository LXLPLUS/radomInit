package com.lxkplus.RandomInit.model.VO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Data
public class BuildRuler {
    @Min(1)
    @Max(1000)
    Integer count;
    String rulerType;
    List<String> params;
}
