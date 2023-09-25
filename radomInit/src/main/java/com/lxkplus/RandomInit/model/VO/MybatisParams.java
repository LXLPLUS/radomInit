package com.lxkplus.RandomInit.model.VO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MybatisParams {
    boolean boxed;
    boolean valid;
    boolean allString;
    boolean defaultTableName;
    boolean addComment;
}
