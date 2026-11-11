package com.blog.controller;

import com.blog.common.PageResult;
import com.blog.common.Result;
import com.blog.config.RateLimit;
import com.blog.dto.request.ArticleQuery;
import com.blog.dto.request.ArticleRequest;
import com.blog.dto.response.ArticleVO;
import com.blog.security.UserContext;
import com.blog.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 文章控制器
 *
 * 公开接口: GET /blogs (列表), GET /blogs/{id} (详情)
 * 需登录: POST /blogs, PUT /blogs/{id}, DELETE /blogs/{id}
 */
@Tag(name = "文章", description = "博客文章 CRUD")
@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @Operation(summary = "文章列表 (分页 + 筛选)")
    @GetMapping
    public Result<PageResult<ArticleVO>> list(ArticleQuery query) {
        return articleService.pageList(query).toResult();
    }

    @Operation(summary = "文章详情")
    @GetMapping("/{id}")
    public Result<ArticleVO> detail(@PathVariable Long id) {
        return Result.success(articleService.detail(id));
    }

    @Operation(summary = "点赞")
    @PostMapping("/{id}/like")
    public Result<Void> like(@PathVariable Long id) {
        articleService.like(id);
        return Result.success();
    }

    @Operation(summary = "创建文章 (需登录)")
    @PostMapping
    @RateLimit(permits = 5, seconds = 60)   // 限流: 60秒最多发5篇
    public Result<ArticleVO> create(@Valid @RequestBody ArticleRequest req) {
        Long authorId = UserContext.getUserId();
        return Result.success("发布成功", articleService.create(req, authorId));
    }

    @Operation(summary = "更新文章 (仅作者或管理员)")
    @PutMapping("/{id}")
    public Result<ArticleVO> update(@PathVariable Long id,
                                    @Valid @RequestBody ArticleRequest req) {
        return Result.success("更新成功",
                articleService.update(id, req, UserContext.getUserId()));
    }

    @Operation(summary = "删除文章 (仅作者或管理员)")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        articleService.delete(id, UserContext.getUserId());
        return Result.success("删除成功", null);
    }
}
