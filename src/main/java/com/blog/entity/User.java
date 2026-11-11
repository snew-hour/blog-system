package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体
 *
 * MyBatis-Plus 注解速查:
 *   @TableId       主键策略 (AUTO = 自增)
 *   @TableField    字段映射策略 (EXIST = 该字段存在于表中)
 *   @TableLogic    逻辑删除 (查询时自动加 deleted=0 条件)
 *   @TableField(fill)  自动填充 (配合 MetaObjectHandler)
 */
@Data
@TableName("sys_user")
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String avatar;

    private String email;

    private String phone;

    /** 角色: USER / ADMIN */
    private String role;

    /** 状态: 1启用 0禁用 */
    private Integer status;

    private LocalDateTime lastLogin;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
