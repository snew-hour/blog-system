package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /** 文章浏览量 +1 (SQL 层面原子操作, 比 Java read-modify-write 更高效安全) */
    @Update("UPDATE blog_article SET views = views + 1 WHERE id = #{id}")
    int incrementViews(@Param("id") Long id);

    /** 点赞数 +1 */
    @Update("UPDATE blog_article SET likes = likes + 1 WHERE id = #{id}")
    int incrementLikes(@Param("id") Long id);
}
