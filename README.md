# 校园失物招领智能匹配系统

## 启动步骤

### 1. 创建数据库
```sql
-- 登录 MySQL 后执行
source database/schema.sql
```
或者手动执行 `database/schema.sql` 文件中的 SQL 语句

### 2. 修改数据库配置
编辑 `src/main/resources/application.yml`，修改数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/campus_lost_found?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root        # 改成你的用户名
    password: root        # 改成你的密码
```

### 3. 运行项目
```bash
# 方式1: Maven 命令
mvn spring-boot:run

# 方式2: IDE 中运行 MyClassProjectApplication.java
```

### 4. 访问系统
打开浏览器访问: http://localhost:8080

## 常见问题

### 404 错误
1. 确认 MySQL 服务已启动
2. 确认数据库 `campus_lost_found` 已创建
3. 确认数据库用户名密码正确

### 数据库连接失败
检查 MySQL 是否允许远程连接，执行：
```sql
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'your_password';
FLUSH PRIVILEGES;
```

## 功能说明

| 模块 | 功能 |
|------|------|
| 用户管理 | 注册、登录、个人信息 |
| 失物发布 | 发布丢失物品、上传图片 |
| 拾物发布 | 发布拾到物品、上传图片 |
| 智能匹配 | 自动匹配失物和拾物 |
| 认领管理 | 发起认领、确认/拒绝 |
| 消息通知 | 匹配和认领通知 |
| 公告统计 | 公告列表、数据统计 |

## 技术栈

- 后端: Spring Boot 2.6 + MyBatis-Plus + MySQL
- 前端: 原生 HTML/CSS/JavaScript
- 认证: JWT Token
