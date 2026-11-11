package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 *
 * BaseMapper<User> 已经提供:
 *   - selectById / selectOne / selectList / selectPage
 *   - insert / updateById / deleteById / delete
 * 基本 CRUD 不需要写 SQL!
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
