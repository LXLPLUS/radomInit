package com.lxkplus.RandomInit.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Validated
@Data
@NoArgsConstructor
public class RandomParam {
    @NotNull
    String columnName;
    @DecimalMin("1")
    @NotNull
    Integer count;
    String builderRuler;
    String regex;
    Integer startIndex;
    Integer min;
    Integer max;
    boolean sorted;
    Date startTime;
    Date endTime;
}
