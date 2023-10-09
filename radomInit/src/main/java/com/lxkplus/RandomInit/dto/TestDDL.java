package com.lxkplus.RandomInit.dto;

import lombok.Data;

@Data
public class TestDDL {
    /**
     * 是否通过格式校验
     */
    boolean pass = false;
    /**
     * 不通过校验的原因和故障信息
     */
    String message;
}
