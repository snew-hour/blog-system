# Blog System - 从零运行到 GitHub 完整教程

> 适合：Windows 11 + Java 17 + Maven + Git 已安装，但 MySQL/Redis 还没装
> 目标：30 分钟内让项目跑起来，并推到 GitHub

---

## 第一步：先跑起来（不用装 MySQL/Redis）

### 1.1 打开项目

```bash
cd C:\Users\houri\Documents\Trae-CN\temp_20261110_01\blog-system
```

### 1.2 修改配置（H2 替代 MySQL）

我已经帮你改了配置文件，用的是 **H2 内存数据库** + **本地缓存**，不需要安装任何数据库。

直接启动：

```bash
mvn spring-boot:run
```

等待 30-60 秒（第一次要下载依赖），看到这句就成功了：

```
Started BlogApplication in 3.456 seconds
```

### 1.3 验证接口

浏览器打开：
- **Swagger 文档**：http://localhost:8080/api/doc.html
- **健康检查**：http://localhost:8080/api/health

Swagger 页面里找到 `/auth/login`，点击 Try it out，输入：
```json
{
  "username": "admin",
  "password": "admin123"
}
```
返回 token 就说明登录接口通了。

---

## 第二步：推到 GitHub

### 2.1 在 GitHub 上创建仓库

1. 打开 https://github.com/new
2. Repository name 填：`blog-system`
3. Description 填：`Spring Boot 3.x 博客系统 - JWT + Redis + MyBatis-Plus`
4. 选 **Public**
5. **不要**勾选 "Add a README file"（我们本地有）
6. 点击 Create repository

### 2.2 本地 Git 操作

```bash
cd C:\Users\houri\Documents\Trae-CN\temp_20261110_01\blog-system

# 初始化
git init

# 添加所有文件
git add .

# 提交
git commit -m "feat: Spring Boot blog system with JWT + Redis + MyBatis-Plus

- 用户认证：JWT Token + BCrypt 加密
- 权限控制：RBAC，作者/管理员分级
- 缓存优化：Redis JSON 序列化
- 接口限流：Lua 滑动窗口
- API 文档：Knife4j (Swagger)
- 数据库：MyBatis-Plus + MySQL/H2"

# 关联远程仓库（把 snew-hour 换成你的用户名）
git branch -M main
git remote add origin https://github.com/snew-hour/blog-system.git

# 推送
git push -u origin main
```

### 2.3 验证

打开 https://github.com/snew-hour/blog-system，看到代码就说明成功了。

---

## 第三步：写一份专业的 README

这是面试官点进来第一眼看到的东西，必须写好。

把下面内容保存为 `blog-system/README.md`：

```markdown
# Blog System

基于 Spring Boot 3.x 的现代化博客后端系统，提供完整的用户认证、文章管理、分类管理和评论功能。

## 技术栈

| 组件 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.x | 核心框架 |
| MyBatis-Plus | 3.5.x | ORM 框架 |
| MySQL / H2 | 8.x / 2.x | 数据库 |
| Redis | 7.x | 缓存（本地模式用 Caffeine） |
| JWT | 0.11.x | 认证令牌 |
| Knife4j | 4.x | API 文档 |
| BCrypt | - | 密码加密 |

## 核心功能

- **用户认证**：JWT Token 登录/注册/登出，BCrypt 密码加密
- **权限控制**：RBAC 模型，区分普通用户/管理员/作者
- **文章管理**：CRUD + 分页 + 分类筛选 + 点赞
- **接口限流**：基于 Lua 脚本的滑动窗口限流
- **缓存优化**：Redis 缓存热点数据，JSON 序列化
- **API 文档**：Knife4j 在线调试接口

## 项目结构

```
src/main/java/com/blog/
├── common/          # 统一响应、异常处理、分页
├── config/          # MyBatis-Plus/Redis/Swagger/跨域配置
├── controller/      # REST API 入口
├── dto/             # 请求/响应对象
├── entity/          # 数据库实体
├── mapper/          # MyBatis-Plus Mapper
├── security/        # JWT 鉴权、拦截器、用户上下文
├── service/         # 业务逻辑
└── util/            # 工具类
```

## 快速启动

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.x（或 H2 内存模式）

### 运行步骤

```bash
# 1. 克隆项目
git clone https://github.com/snew-hour/blog-system.git
cd blog-system

# 2. 配置数据库（可选，默认 H2 内存模式）
# 编辑 src/main/resources/application.yml

# 3. 启动
mvn spring-boot:run

# 4. 访问 Swagger
# http://localhost:8080/api/doc.html
```

### 默认账号
- 用户名：admin
- 密码：admin123

## 接口预览

| 模块 | 接口 | 说明 |
|------|------|------|
| 认证 | POST /auth/login | 登录获取 Token |
| 认证 | POST /auth/register | 注册 |
| 认证 | POST /auth/logout | 登出（Token 黑名单） |
| 文章 | GET /articles | 分页查询文章 |
| 文章 | POST /articles | 发布文章 |
| 文章 | PUT /articles/{id} | 更新文章 |
| 文章 | DELETE /articles/{id} | 删除文章 |
| 分类 | GET /categories | 查询所有分类 |

## 设计亮点

1. **JWT 无状态认证**：Token 黑名单实现安全登出
2. **ThreadLocal 用户上下文**：JwtInterceptor 解析 Token 后写入，Controller 直接获取当前用户
3. **统一异常处理**：@RestControllerAdvice 全局捕获，返回标准错误码
4. **缓存策略**：文章详情 Redis 缓存，过期时间随机防雪崩
5. **限流保护**：AOP + Lua 实现接口级限流

## 后续规划

- [ ] 接入 Elasticsearch 实现全文搜索
- [ ] 集成 MinIO/OSS 实现图片上传
- [ ] 添加 Docker Compose 一键部署
- [ ] 接入 Spring Cache 注解式缓存

---

**作者**：石芸菲  
**GitHub**：https://github.com/snew-hour  
**联系**：hour88@qq.com
```

---

## 第四步：README 推送到 GitHub

```bash
cd C:\Users\houri\Documents\Trae-CN\temp_20261110_01\blog-system

git add README.md
git commit -m "docs: add comprehensive README with tech stack and API docs"
git push
```

---

## 第五步：后续升级（装 MySQL/Redis 后）

等你装了 MySQL 和 Redis，把配置改回来：

1. 安装 MySQL 8.x，创建数据库 `blog_system`
2. 安装 Redis（Windows 可以用 Memurai 或 WSL）
3. 修改 `application.yml` 里的数据源配置
4. 执行 `sql/schema.sql` 初始化数据
5. 重新启动

---

## 常见问题

**Q: mvn spring-boot:run 报错 "找不到符号"？**
A: 执行 `mvn clean compile` 先编译。

**Q: 端口 8080 被占用？**
A: 改 `application.yml` 里的 `server.port: 8081`。

**Q: Swagger 打不开？**
A: 检查是否启动了，或者访问 http://localhost:8080/api/doc.html

**Q: 想换数据库？**
A: 改 `pom.xml` 里的依赖和 `application.yml` 里的配置。

---

## 检查清单

- [ ] 本地能启动，Swagger 能打开
- [ ] GitHub 仓库已创建，代码已推送
- [ ] README.md 已上传，排版正常
- [ ] 简历里的 GitHub 链接点进去能看到项目

全部打勾，你的简历就有硬货了。
