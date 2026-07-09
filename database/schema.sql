-- 校园失物招领智能匹配系统 - 建表语句

CREATE DATABASE IF NOT EXISTS campus_lost_found DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus_lost_found;

-- 用户表
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) NOT NULL UNIQUE COMMENT '学号',
    phone VARCHAR(11) COMMENT '手机号',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    avatar VARCHAR(255) COMMENT '头像',
    college VARCHAR(100) COMMENT '学院',
    email VARCHAR(100) COMMENT '邮箱',
    role TINYINT DEFAULT 0 COMMENT '0普通用户 1管理员',
    status TINYINT DEFAULT 1 COMMENT '0禁用 1正常',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '用户表';

-- 物品分类表
CREATE TABLE category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '分类名称',
    icon VARCHAR(50) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '物品分类表';

-- 失物信息表
CREATE TABLE lost_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '发布者ID',
    title VARCHAR(100) NOT NULL COMMENT '物品名称',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    description TEXT COMMENT '物品描述',
    lost_location VARCHAR(200) NOT NULL COMMENT '丢失地点',
    lost_time DATETIME NOT NULL COMMENT '丢失时间',
    contact_info VARCHAR(100) NOT NULL COMMENT '联系方式',
    image_url LONGTEXT COMMENT '图片(Base64)',
    status TINYINT DEFAULT 0 COMMENT '0待匹配 1待认领 2已认领 3已过期',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
) COMMENT '失物信息表';

-- 拾物信息表
CREATE TABLE found_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '发布者ID',
    title VARCHAR(100) NOT NULL COMMENT '物品名称',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    description TEXT COMMENT '物品描述',
    found_location VARCHAR(200) NOT NULL COMMENT '拾取地点',
    found_time DATETIME NOT NULL COMMENT '拾取时间',
    storage_location VARCHAR(200) NOT NULL COMMENT '存放位置',
    image_url LONGTEXT COMMENT '图片(Base64)',
    status TINYINT DEFAULT 0 COMMENT '0待匹配 1待认领 2已认领 3已过期',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
) COMMENT '拾物信息表';

-- 匹配记录表
CREATE TABLE match_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lost_item_id BIGINT NOT NULL COMMENT '失物ID',
    found_item_id BIGINT NOT NULL COMMENT '拾物ID',
    similarity_score DOUBLE NOT NULL COMMENT '相似度0-100',
    match_type VARCHAR(50) COMMENT '匹配类型',
    status TINYINT DEFAULT 0 COMMENT '0待确认 1已确认 2已拒绝',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lost_item_id) REFERENCES lost_item(id),
    FOREIGN KEY (found_item_id) REFERENCES found_item(id)
) COMMENT '匹配记录表';

-- 认领申请表
CREATE TABLE claim_request (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lost_item_id BIGINT COMMENT '失物ID',
    found_item_id BIGINT COMMENT '拾物ID',
    claimer_id BIGINT NOT NULL COMMENT '认领者ID',
    owner_id BIGINT NOT NULL COMMENT '拾取者ID',
    description TEXT COMMENT '认领说明',
    proof_image VARCHAR(500) COMMENT '证明图片',
    status TINYINT DEFAULT 0 COMMENT '0待确认 1已确认 2已拒绝 3已取消',
    admin_remark TEXT COMMENT '管理员备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (lost_item_id) REFERENCES lost_item(id),
    FOREIGN KEY (found_item_id) REFERENCES found_item(id),
    FOREIGN KEY (claimer_id) REFERENCES users(id),
    FOREIGN KEY (owner_id) REFERENCES users(id)
) COMMENT '认领申请表';

-- 消息通知表
CREATE TABLE notification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '接收者ID',
    title VARCHAR(100) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    type TINYINT NOT NULL COMMENT '1匹配 2认领 3系统 4公告',
    related_id BIGINT COMMENT '关联ID',
    is_read TINYINT DEFAULT 0 COMMENT '0未读 1已读',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
) COMMENT '消息通知表';

-- 公告表
CREATE TABLE announcement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    publisher_id BIGINT NOT NULL COMMENT '发布者ID',
    is_top TINYINT DEFAULT 0 COMMENT '0不置顶 1置顶',
    status TINYINT DEFAULT 1 COMMENT '0草稿 1已发布 2已下架',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (publisher_id) REFERENCES users(id)
) COMMENT '公告表';

-- 插入默认分类
INSERT INTO category (name, icon, sort_order) VALUES
('电子设备', 'fa-laptop', 1),
('书本文具', 'fa-book', 2),
('证件卡类', 'fa-id-card', 3),
('衣物饰品', 'fa-tshirt', 4),
('钥匙雨伞', 'fa-key', 5),
('钱包背包', 'fa-wallet', 6),
('体育用品', 'fa-football', 7),
('其他物品', 'fa-box', 8);
