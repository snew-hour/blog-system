-- ============================================================
-- 博客管理系统 数据库初始化脚本
-- MySQL 8.x
-- ============================================================

CREATE DATABASE IF NOT EXISTS blog_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE blog_db;

-- ========== 用户表 ==========
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    username     VARCHAR(50)  NOT NULL COMMENT '用户名',
    password     VARCHAR(100) NOT NULL COMMENT '密码(BCrypt加密)',
    nickname     VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
    avatar       VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    email        VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    phone        VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    role         VARCHAR(20)  NOT NULL DEFAULT 'USER' COMMENT '角色: USER/ADMIN',
    status       TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 1启用 0禁用',
    last_login   DATETIME     DEFAULT NULL COMMENT '最后登录时间',
    create_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted      TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0未删除 1已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB COMMENT='用户表';

-- ========== 分类表 ==========
DROP TABLE IF EXISTS blog_category;
CREATE TABLE blog_category (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    name         VARCHAR(50)  NOT NULL COMMENT '分类名称',
    description  VARCHAR(200) DEFAULT NULL COMMENT '描述',
    sort_order   INT          NOT NULL DEFAULT 0 COMMENT '排序',
    create_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted      TINYINT      NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB COMMENT='博客分类';

-- ========== 博客文章表 ==========
DROP TABLE IF EXISTS blog_article;
CREATE TABLE blog_article (
    id           BIGINT        NOT NULL AUTO_INCREMENT,
    title        VARCHAR(200)  NOT NULL COMMENT '标题',
    summary      VARCHAR(500)  DEFAULT NULL COMMENT '摘要',
    content     MEDIUMTEXT    NOT NULL COMMENT '正文内容(Markdown)',
    cover        VARCHAR(255)  DEFAULT NULL COMMENT '封面图',
    category_id  BIGINT        DEFAULT NULL COMMENT '分类ID',
    author_id    BIGINT        NOT NULL COMMENT '作者ID',
    views        INT           NOT NULL DEFAULT 0 COMMENT '浏览量',
    likes        INT           NOT NULL DEFAULT 0 COMMENT '点赞数',
    status       TINYINT       NOT NULL DEFAULT 1 COMMENT '状态: 1已发布 0草稿',
    is_top       TINYINT       NOT NULL DEFAULT 0 COMMENT '是否置顶: 1是 0否',
    create_time  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted      TINYINT       NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_category (category_id),
    KEY idx_author (author_id),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB COMMENT='博客文章';

-- ========== 评论表 ==========
DROP TABLE IF EXISTS blog_comment;
CREATE TABLE blog_comment (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    article_id   BIGINT       NOT NULL COMMENT '文章ID',
    user_id      BIGINT       NOT NULL COMMENT '评论者ID',
    parent_id    BIGINT       DEFAULT NULL COMMENT '父评论ID(楼中楼)',
    content      VARCHAR(1000) NOT NULL COMMENT '评论内容',
    status       TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 1正常 0审核中',
    create_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted      TINYINT      NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_article (article_id),
    KEY idx_user (user_id)
) ENGINE=InnoDB COMMENT='评论';


-- ========== 初始化数据 ==========

-- 管理员账号 (密码: admin123, BCrypt 加密)
INSERT INTO sys_user (username, password, nickname, role) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '超级管理员', 'ADMIN'),
('demo',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '演示用户',   'USER');

-- 分类
INSERT INTO blog_category (name, description, sort_order) VALUES
('技术分享', '后端、前端、架构等技术文章', 1),
('生活随笔', '日常感悟与思考', 2),
('读书笔记', '读过的好书记录', 3);

-- 示例文章
INSERT INTO blog_article (title, summary, content, category_id, author_id, status) VALUES
(
 'Spring Boot 3.x 快速入门',
 '从零搭建一个 Spring Boot 3 项目，涵盖 MyBatis-Plus、Redis、JWT 集成',
 '# Spring Boot 3.x 快速入门\n\nSpring Boot 让 Java 后端开发变得前所未有地简单...\n\n## 特性\n- 自动配置\n- 起步依赖\n- 内嵌服务器',
 1, 1, 1
);
