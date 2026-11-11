package com.blog.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** 文章创建 / 更新请求 */
@Data
@Schema(description = "文章请求")
public class ArticleRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 200)
    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "摘要")
    private String summary;

    @NotBlank(message = "正文不能为空")
    @Schema(description = "正文内容 (Markdown)")
    private String content;

    @Schema(description = "封面图 URL")
    private String cover;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "状态: 1已发布 0草稿")
    private Integer status = 1;
}
