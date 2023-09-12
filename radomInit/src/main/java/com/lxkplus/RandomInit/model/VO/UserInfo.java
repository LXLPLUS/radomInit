package com.lxkplus.RandomInit.model.VO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserInfo {
    @NotNull
    String userName;
    @NotNull
    String password;
}
