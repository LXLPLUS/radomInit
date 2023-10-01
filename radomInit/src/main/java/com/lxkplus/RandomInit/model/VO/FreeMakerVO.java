package com.lxkplus.RandomInit.model.VO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Valid
public class FreeMakerVO {
    @NotNull
    String template;

    @NotNull
    Map<String, String> params = new HashMap<>();
}
