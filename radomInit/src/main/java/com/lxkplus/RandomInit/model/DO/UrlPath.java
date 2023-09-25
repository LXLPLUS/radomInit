package com.lxkplus.RandomInit.model.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@TableName("url_path")
public class UrlPath {
    /**
     * id
     */
    @TableId(
            type = IdType.AUTO
    )
    @NotNull
    private Integer id;

    /**
     * uuid
     */
    @NotNull
    private String uuid;

    @NotNull
    private String actionId;

    /**
     * 新建时间
     */
    @NotNull
    private Date createTime;

    /**
     * 是否删除
     */
    @NotNull
    private Boolean isDelete;
}
