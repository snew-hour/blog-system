package com.blog.dto.response;

import com.blog.entity.Article;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/** 文章 VO */
@Data
@Schema(description = "文章信息")
public class ArticleVO {

    private Long id;
    private String title;
    private String summary;
    /** 列表页不返回完整 content, 省流量 */
    @Schema(description = "正文 (仅详情接口返回)")
    private String content;
    private String cover;
    private Long categoryId;
    private String categoryName;
    private Long authorId;
    private String authorName;
    private Integer views;
    private Integer likes;
    private Integer status;
    private Integer isTop;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /** Entity -> VO */
    public static ArticleVO from(Article a) {
        if (a == null) return null;
        ArticleVO vo = new ArticleVO();
        vo.setId(a.getId());
        vo.setTitle(a.getTitle());
        vo.setSummary(a.getSummary());
        vo.setContent(a.getContent());
        vo.setCover(a.getCover());
        vo.setCategoryId(a.getCategoryId());
        vo.setAuthorId(a.getAuthorId());
        vo.setViews(a.getViews());
        vo.setLikes(a.getLikes());
        vo.setStatus(a.getStatus());
        vo.setIsTop(a.getIsTop());
        vo.setCreateTime(a.getCreateTime());
        vo.setUpdateTime(a.getUpdateTime());
        return vo;
    }

    /** 列表精简 (去掉 content 省流量) */
    public ArticleVO toListVO() {
        this.content = null;
        return this;
    }
}
