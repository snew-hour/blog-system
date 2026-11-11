package com.blog.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果封装
 *
 * @param <T> 数据列表类型
 */
@Data
public class PageResult<T> implements Serializable {

    private Long total;          // 总记录数
    private Long pages;          // 总页数
    private Integer current;     // 当前页
    private Integer size;        // 每页条数
    private List<T> records;     // 数据列表

    public PageResult() {}

    public PageResult(Long total, Long pages, Integer current, Integer size, List<T> records) {
        this.total = total;
        this.pages = pages;
        this.current = current;
        this.size = size;
        this.records = records;
    }

    /**
     * 从 MyBatis-Plus 的 IPage 转换
     */
    public static <T> PageResult<T> from(com.baomidou.mybatisplus.core.metadata.IPage<T> page) {
        return new PageResult<>(
                page.getTotal(),
                page.getPages(),
                (int) page.getCurrent(),
                (int) page.getSize(),
                page.getRecords()
        );
    }

    /**
     * 包一层 Result
     */
    public Result<PageResult<T>> toResult() {
        return Result.success(this);
    }
}
