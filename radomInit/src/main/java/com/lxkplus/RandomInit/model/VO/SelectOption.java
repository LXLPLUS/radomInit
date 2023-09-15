package com.lxkplus.RandomInit.model.VO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SelectOption {
    String label;
    String rulerType;
    List<String> params;
}
