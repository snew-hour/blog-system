package com.blog.service;

import com.blog.common.PageResult;
import com.blog.dto.request.ArticleQuery;
import com.blog.dto.request.ArticleRequest;
import com.blog.dto.response.ArticleVO;

public interface ArticleService {

    /** 分页查询文章列表 (公开) */
    PageResult<ArticleVO> pageList(ArticleQuery query);

    /** 文章详情 (浏览量 +1) */
    ArticleVO detail(Long id);

    /** 创建文章 */
    ArticleVO create(ArticleRequest req, Long authorId);

    /** 更新文章 (仅作者或管理员) */
    ArticleVO update(Long id, ArticleRequest req, Long userId);

    /** 删除文章 (仅作者或管理员) */
    void delete(Long id, Long userId);

    /** 点赞 +1 */
    void like(Long id);
}
