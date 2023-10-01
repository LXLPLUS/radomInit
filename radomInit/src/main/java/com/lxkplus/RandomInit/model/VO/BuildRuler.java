package com.lxkplus.RandomInit.model.VO;

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
    String rulerType;
    List<String> params;
}
