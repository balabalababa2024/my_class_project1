@echo off
echo ========================================
echo 校园失物招领系统 - 数据库初始化
echo ========================================
echo.

set /p mysql_user="请输入MySQL用户名 (默认root): "
if "%mysql_user%"=="" set mysql_user=root

set /p mysql_pass="请输入MySQL密码: "

echo.
echo 正在创建数据库...
mysql -u%mysql_user% -p%mysql_pass% -e "CREATE DATABASE IF NOT EXISTS campus_lost_found DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

if %errorlevel% neq 0 (
    echo 数据库创建失败！请检查MySQL连接信息
    pause
    exit /b 1
)

echo 正在导入表结构...
mysql -u%mysql_user% -p%mysql_pass% campus_lost_found < database\schema.sql

if %errorlevel% neq 0 (
    echo 表结构导入失败！
    pause
    exit /b 1
)

echo.
echo ========================================
echo 数据库初始化成功！
echo ========================================
echo.
echo 请修改 src/main/resources/application.yml 中的数据库密码
echo 然后运行: mvn spring-boot:run
echo.
pause
