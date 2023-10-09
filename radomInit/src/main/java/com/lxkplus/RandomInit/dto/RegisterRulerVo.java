package com.lxkplus.RandomInit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRulerVo {
    String actionID;
    @NotNull
    String rulerType;
    @NotNull
    String params;
}
