package com.blog.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 文章列表查询参数 */
@Data
@Schema(description = "文章查询参数")
public class ArticleQuery {

    @Schema(description = "关键词 (模糊匹配标题)")
    private String keyword;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "作者ID")
    private Long authorId;

    @Schema(description = "状态: 1已发布 0草稿")
    private Integer status;

    @Schema(description = "当前页", defaultValue = "1")
    private Integer current = 1;

    @Schema(description = "每页条数", defaultValue = "10")
    private Integer size = 10;
}
