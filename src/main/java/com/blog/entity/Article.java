package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 博客文章实体
 */
@Data
@TableName("blog_article")
public class Article implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String summary;

    /** Markdown 正文 */
    private String content;

    private String cover;

    private Long categoryId;

    private Long authorId;

    private Integer views;

    private Integer likes;

    /** 状态: 1已发布 0草稿 */
    private Integer status;

    private Integer isTop;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
